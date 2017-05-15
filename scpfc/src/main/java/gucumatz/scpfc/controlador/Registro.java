package gucumatz.scpfc.controlador;

import gucumatz.scpfc.modelo.Usuario;
import gucumatz.scpfc.modelo.db.ControladorJpaUsuario;
import gucumatz.scpfc.modelo.db.FabricaControladorJpa;
import gucumatz.scpfc.modelo.db.exceptions.IllegalOrphanException;
import gucumatz.scpfc.modelo.db.exceptions.NonexistentEntityException;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Random;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.validator.ValidatorException;
import javax.mail.MessagingException;

import org.primefaces.model.UploadedFile;

/**
 * Clase que implementa el registro de un nuevo usuario.
 *
 * @author lchacon
 */
@ManagedBean
@ViewScoped
public class Registro implements Serializable {

    /* Las contraseñase deben tener al menos 4 caracteres. */
    private static final int CONTRASENA_TAM_MIN = 4;
    /* Las imágenes deben tener tamaño máximo de 4MB. */
    private static final int FOTO_TAM_MAX = 4 * 1024 * 1024;

    /* Mensajes de error. */
    private static final String MENSAJE_USUARIO_NO_DISPONIBLE
            = "Ya existe una cuenta con este nombre de usuario";
    private static final String MENSAJE_CORREO_NO_DISPONIBLE
            = "Ya existe una cuenta con este correo";
    private static final String MENSAJE_CORREO_NO_CIENCIAS
            = "Debes proporcionar un correo @ciencias.unam.mx";
    private static final String MENSAJE_CORREO_NO_VALIDO
            = "Esta no es una dirección de correo válida.";
    private static final String MENSAJE_CONTRASENA_CORTA
            = "La contraseña debe tener al menos 4 caracteres";
    private static final String MENSAJE_CONFIRMACION_INCORRECTA
            = "La confirmación de contraseña no coincide";
    private static final String MENSAJE_FOTO_GRANDE
            = "La fotografía no puede ser más grande que 4MB";
    private static final String MENSAJE_FOTO_TIPO_INVALIDO
            = "Sólo se aceptan fotografías en formato JPG o PNG";

    private static final String MENSAJE_CORREO_ENVIADO
        =  "Se ha reenviado el correo de confirmación a tu dirección de correo";
    private static final String MENSAJE_CORREO_NO_ENVIADO
        = "No se ha podido enviar el correo de confirmación."
        + " Vuelve a intentarlo más tarde.";

    /* Único dominio aceptado en los correos electrónicos. */
    private static final String DOMINIO_CORREO = "@ciencias.unam.mx";

    /**
     * Controlador JPA para acceder a la BD.
     */
    private final ControladorJpaUsuario jpaUsuario;

    /**
     * Nombre del nuevo usuario.
     */
    private String nombreDeUsuario;

    /**
     * Correo electrónico del nuevo usuario.
     */
    private String correoElectronico;

    /**
     * Contraseña del nuevo usuario.
     */
    private String contrasena;

    /**
     * Fotografía del usuario.
     */
    private UploadedFile foto;

    /**
     * Extensión de la fotografía del usuario.
     */
    private String extensionFoto;

    public Registro() {
        jpaUsuario = new FabricaControladorJpa().obtenerControladorJpaUsuario();
    }

    public String getNombreDeUsuario() {
        return nombreDeUsuario;
    }

    public void setNombreDeUsuario(String nombreDeUsuario) {
        this.nombreDeUsuario = nombreDeUsuario;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public UploadedFile getFoto() {
        return foto;
    }

    public void setFoto(UploadedFile foto) {
        this.foto = foto;
    }

    /**
     * Registra a un usuario con los datos recibidos.
     *
     * @return la siguiente página a mostrar.
     */
    public String registrar() {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        Usuario u = new Usuario();
        u.setNombre(nombreDeUsuario);
        u.setCorreoElectronico(correoElectronico);
        u.setContrasena(contrasena);
        u.setConfirmado(false);
        u.setEsAdministrador(false);
        u.setCodigoDeActivacion(obtenerCadenaAleatoria());

        jpaUsuario.crear(u);

        try {
            enviarCorreoDeActivacion(u);

            FacesMessage facesMessage
                = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    MENSAJE_CORREO_ENVIADO + correoElectronico, null);
            facesContext.addMessage(null, facesMessage);
        } catch (MessagingException e) {
            FacesMessage facesMessage
                    = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        MENSAJE_CORREO_NO_ENVIADO, null);
            facesContext.addMessage(null, facesMessage);

            /* Si no se pudo enviar el correo, eliminamos al usuario. */
            try {
                jpaUsuario.destruir(u.getId());
            } catch (NonexistentEntityException | IllegalOrphanException nee) {
            }
            return null;
        }

        if (extensionFoto != null) {
            try {
                guardarFoto(u);
            } catch (Exception e) {
                FacesMessage facesMessage
                        = new FacesMessage(FacesMessage.SEVERITY_WARN,
                                "Ocurrió un error al guardar tu foto.", null);
                facesContext.addMessage(null, facesMessage);
            }
        }

        facesContext.getExternalContext().getFlash().setKeepMessages(true);
        return "index?faces-redirect=true";
    }

    /**
     * Comprueba que el nombre de usuario esté disponible.
     *
     * @param context FacesContext para la solicitud que se está procesando
     * @param component el componente que se está revisando
     * @param value el valor que se quiere validar
     * @throws ValidatorException si el valor dado no es válido
     */
    public void validarNombreDeUsuario(FacesContext context,
                                       UIComponent component,
                                       Object value)
            throws ValidatorException {
        String nombreDeUsuarioPorValidar = (String) value;

        /* Si es vacío, el atributo required lo rechazará. */
        if (nombreDeUsuarioPorValidar == null
                || nombreDeUsuarioPorValidar.isEmpty()) {
            return;
        }

        /* Verifica que el nombre esté disponible. */
        Usuario usuario = jpaUsuario.buscarPorNombre(nombreDeUsuarioPorValidar);
        if (usuario != null) {
            FacesMessage mensajeDeError
                = crearMensajeDeError(MENSAJE_USUARIO_NO_DISPONIBLE);
            throw new ValidatorException(mensajeDeError);
        }
    }

    /**
     * Comprueba que el correo electrónico sea de @ciencias.unam.mx y que no se
     * haya usado para otra cuenta.
     *
     * @param context FacesContext para la solicitud que se está procesando
     * @param component el componente que se está revisando
     * @param value el valor que se quiere validar
     * @throws ValidatorException si el valor dado no es válido
     */
    public void validarCorreoElectronico(FacesContext context,
                                         UIComponent component,
                                         Object value)
            throws ValidatorException {
        String correoPorValidar = (String) value;

        /* Si es vacío, el atributo required lo rechazará. */
        if (correoPorValidar == null
                || correoPorValidar.isEmpty()) {
            return;
        }

        /* Verifica que el correo sea de @ciencias.unam.mx. */
        if (!correoPorValidar.endsWith(DOMINIO_CORREO)) {
            FacesMessage mensajeDeError
                = crearMensajeDeError(MENSAJE_CORREO_NO_CIENCIAS);
            throw new ValidatorException(mensajeDeError);
        }

        /* Verifica que el correo tenga una parte local (antes de @). */
        if (correoPorValidar.equals(DOMINIO_CORREO)) {
            FacesMessage mensajeDeError
                = crearMensajeDeError(MENSAJE_CORREO_NO_VALIDO);
            throw new ValidatorException(mensajeDeError);
        }

        /* Verifica que el correo electrónico no se haya usado. */
        Usuario usuario
            = jpaUsuario.buscarPorCorreoElectronico(correoPorValidar);
        if (usuario != null) {
            FacesMessage mensajeDeError
                = crearMensajeDeError(MENSAJE_CORREO_NO_DISPONIBLE);
            throw new ValidatorException(mensajeDeError);
        }
    }

    /**
     * Verifica que la contraseña sea del tamaño adecuado y que la confirmación
     * sea correcta.
     *
     * @param context FacesContext para la solicitud que se está procesando
     * @param component el componente que se está revisando
     * @param value el valor que se quiere validar
     * @throws ValidatorException si el valor dado no es válido
     */
    public void validarContrasena(FacesContext context,
                                  UIComponent component,
                                  Object value)
            throws ValidatorException {
        String contrasenaPorValidar = (String) value;

        /* Obtiene al componente con la confirmación y la extrae. */
        UIInput componenteConfirmacion
            = (UIInput) component.getAttributes().get("confirmacion");
        String confirmacion
            = (String) componenteConfirmacion.getSubmittedValue();

        /* Si alguno está vacío, el atributo required lo rechaza. */
        if (contrasenaPorValidar == null || contrasenaPorValidar.isEmpty()
                || confirmacion == null || confirmacion.isEmpty()) {
            return;
        }

        /* Verifica el tamaño de la contraseña. */
        if (contrasenaPorValidar.length() < CONTRASENA_TAM_MIN) {
            FacesMessage mensajeDeError
                = crearMensajeDeError(MENSAJE_CONTRASENA_CORTA);
            throw new ValidatorException(mensajeDeError);
        }

        /* Verifica que la confirmación coincida. */
        if (!confirmacion.equals(contrasenaPorValidar)) {
            FacesMessage mensajeDeError
                = crearMensajeDeError(MENSAJE_CONFIRMACION_INCORRECTA);
            throw new ValidatorException(mensajeDeError);
        }
    }

    /**
     * Verifica que la foto subida sea del tipo y tamaño adecuado. Establece el
     * valor de extensionFoto.
     *
     * @param context FacesContext para la solicitud que se está procesando
     * @param component el componente que se está revisando
     * @param value el valor que se quiere validar
     * @throws ValidatorException si el valor dado no es válido
     */
    public void validarFoto(FacesContext context,
                            UIComponent component,
                            Object value)
            throws ValidatorException {
        UploadedFile fotoPorValidar = (UploadedFile) value;
        extensionFoto = null;

        /* Si no hay foto, la aceptamos. */
        if (fotoPorValidar == null || fotoPorValidar.getSize() == 0) {
            return;
        }

        /* Verificamos que el tamaño sea adecuado. */
        long tamanoFoto = fotoPorValidar.getSize();
        if (tamanoFoto > FOTO_TAM_MAX) {
            FacesMessage mensajeDeError
                = crearMensajeDeError(MENSAJE_FOTO_GRANDE);
            throw new ValidatorException(mensajeDeError);
        }

        /* Verificamos que el tipo sea adecuado. */
        String nombreDeArchivo = fotoPorValidar.getFileName();
        if (nombreDeArchivo.endsWith(".jpg")
                || nombreDeArchivo.endsWith(".jpeg")) {
            extensionFoto = ".jpg";
        } else if (nombreDeArchivo.endsWith(".png")) {
            extensionFoto = ".png";
        } else {
            FacesMessage mensajeDeError
                = crearMensajeDeError(MENSAJE_FOTO_TIPO_INVALIDO);
            throw new ValidatorException(mensajeDeError);
        }
    }

    /**
     * Agrega un mensaje de error global en el contexto actual si hay errores de
     * validación.
     *
     * @param e (no se usa)
     */
    public void mostrarErrorDeValidacion(ComponentSystemEvent e) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext.isValidationFailed()) {
            FacesMessage facesMessage
                = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Los datos que proporcionaste no son válidos", null);
            facesContext.addMessage(null, facesMessage);
        }
    }

    /**
     * Crea un nuevo mensaje de error. El mensaje no contiene detalles y tiene
     * severidad de error.
     *
     * @param mensaje el mensaje de error
     * @return un FacesMessage con severidad error y el mensaje dado
     */
    private FacesMessage crearMensajeDeError(String mensaje) {
        return new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, null);
    }

    /**
     * Envia el correo de activación para el usuario dado.
     *
     * @param usuario el usuario al que se le envia el correo
     * @throws MessagingException si hubo un error al enviar el correo
     */
    private void enviarCorreoDeActivacion(Usuario usuario)
            throws MessagingException {
        try {
            CorreoDeActivacion correo = new CorreoDeActivacion(usuario);
            correo.enviar();
        } catch (IOException ioe) {
            throw new MessagingException();
        }
    }

    /**
     * Guarda la foto de un usuario en el disco.
     *
     * @param usuario el usuario con el que se asocia la foto
     * @throws Exception si ocurre un error al guardar la foto
     */
    private void guardarFoto(Usuario usuario)
            throws Exception {
        String nombreDeArchivo = "usuario/" + usuario.getId() + extensionFoto;
        ManejadorDeImagenes mdi = new ManejadorDeImagenes();
        mdi.escribirImagen(foto, nombreDeArchivo);

        usuario.setRutaImagen(nombreDeArchivo);
        jpaUsuario.editar(usuario);
    }

    /**
     * Genera una cadena aleatoria para usarse como código de activación.
     *
     * @return una cadena aleatoria de 30 caracteres
     */
    private String obtenerCadenaAleatoria() {
        /* La base que se usa para convertir un número a cadena. */
        final int base = 32;
        /*
         * Tamaño de la cadena aleatoria. Usa 30 caracteres, y cada carácter
         * corresponde a 5 bits.
         */
        final int tam = 30 * 5;

        Random rnd = new Random();
        return new BigInteger(tam, rnd).toString(base);
    }

}
