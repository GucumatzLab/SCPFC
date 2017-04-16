/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gucumatz.scpfc.web;

import gucumatz.scpfc.modelo.Usuario;
import gucumatz.scpfc.modelo.db.FabricaControladorJpa;
import gucumatz.scpfc.modelo.db.UsuarioJpaController;
import java.io.Serializable;
import java.util.Properties;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.model.UploadedFile;

@ManagedBean
@ViewScoped
public class Registro implements Serializable {

    /* Mensajes de error. */
    private static final String MENSAJE_USUARIO_NO_DISPONIBLE
            = "Ya existe una cuenta con este nombre de usuario";
    private static final String MENSAJE_CORREO_NO_DISPONIBLE
            = "Ya existe una cuenta con este correo";
    private static final String MENSAJE_CORREO_NO_CIENCIAS
            = "Debes proporcionar un correo @ciencias.unam.mx";
    private static final String MENSAJE_CONTRASENA_CORTA
            = "La contraseña debe tener al menos 4 caracteres";
    private static final String MENSAJE_CONFIRMACION_INCORRECTA
            = "La confirmación de contraseña no coincide";

    /**
     * Controlador JPA para acceder a la BD.
     */
    private final UsuarioJpaController jpaUsuario;

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

    public String registrar() {
        Usuario u = new Usuario();
        u.setNombre(nombreDeUsuario);
        u.setCorreoElectronico(correoElectronico);
        u.setContrasena(contrasena);
        u.setConfirmada(false);
        u.setEsAdministrador(false);
        u.setCodigoDeActivacion("asdf");

        jpaUsuario.create(u);

        if (foto != null) {
            try {
                String nombreDeArchivo = foto.getFileName();
                String extension = null;
                if (nombreDeArchivo.endsWith(".jpg") || nombreDeArchivo.endsWith(".jpeg")) {
                    extension = ".jpg";
                } else if (nombreDeArchivo.endsWith(".png")) {
                    extension = ".png";
                }

                if (extension != null) {
                    nombreDeArchivo = "usuario/" + u.getId() + extension;
                    ManejadorDeImagenes mdi = new ManejadorDeImagenes();
                    mdi.escribirImagen(foto, nombreDeArchivo);
                }

                u.setRutaImagen(nombreDeArchivo);
                jpaUsuario.edit(u);
            } catch (Exception e) {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                FacesMessage facesMessage
                        = new FacesMessage(FacesMessage.SEVERITY_WARN,
                                "Ocurrió un error al guardar tu foto.", null);
                facesContext.addMessage(null, facesMessage);
                e.printStackTrace();
            }
        }

        enviarCorreoDeActivacion(u);
        return "index";
    }

    /**
     * Comprueba que el nombre de usuario esté disponible.
     */
    public void validarNombreDeUsuario(FacesContext context, UIComponent component, Object value)
            throws ValidatorException {
        String nombreDeUsuario = (String) value;

        /* Si es vacío, el atributo required lo rechazará. */
        if (nombreDeUsuario == null || nombreDeUsuario.isEmpty()) {
            return;
        }

        /* Verifica que el nombre esté disponible. */
        Usuario usuario = jpaUsuario.findByNombre(nombreDeUsuario);
        if (usuario != null) {
            throw new ValidatorException(crearMensajeDeError(MENSAJE_USUARIO_NO_DISPONIBLE));
        }
    }

    /**
     * Comprueba que el correo electrónico sea de @ciencias.unam.mx y que no se
     * haya usado para otra cuenta.
     */
    public void validarCorreoElectronico(FacesContext context, UIComponent component, Object value)
            throws ValidatorException {
        String correoElectronico = (String) value;

        /* Si es vacío, el atributo required lo rechazará. */
        if (correoElectronico == null || correoElectronico.isEmpty()) {
            return;
        }

        /* Verifica que el correo sea de @ciencias.unam.mx. */
        if (!correoElectronico.endsWith("@ciencias.unam.mx")) {
            throw new ValidatorException(crearMensajeDeError(MENSAJE_CORREO_NO_CIENCIAS));
        }

        /* Verifica que el correo electrónico no se haya usado. */
        Usuario usuario = jpaUsuario.findByCorreoElectronico(correoElectronico);
        if (usuario != null) {
            throw new ValidatorException(crearMensajeDeError(MENSAJE_CORREO_NO_DISPONIBLE));
        }
    }

    /**
     * Verifica que la contraseña sea del tamaño adecuado y que la confirmación
     * sea correcta.
     */
    public void validarContrasena(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String contrasena = (String) value;

        /* Obtiene al componente con la confirmación y la extrae. */
        UIInput componenteConfirmacion = (UIInput) component.getAttributes().get("confirmacion");
        String confirmacion = (String) componenteConfirmacion.getSubmittedValue();

        /* Si alguno está vacío, el atributo required lo rechaza. */
        if (contrasena == null || contrasena.isEmpty()
                || confirmacion == null || confirmacion.isEmpty()) {
            return;
        }

        /* Verifica el tamaño de la contraseña. */
        if (contrasena.length() < 4) {
            throw new ValidatorException(crearMensajeDeError(MENSAJE_CONTRASENA_CORTA));
        }

        /* Verifica que la confirmación coincida. */
        if (!confirmacion.equals(contrasena)) {
            throw new ValidatorException(crearMensajeDeError(MENSAJE_CONFIRMACION_INCORRECTA));
        }
    }

    /**
     * Crea un nuevo mensaje de error. El mensaje no contiene detalles y tiene
     * severidad de error.
     */
    private FacesMessage crearMensajeDeError(String mensaje) {
        return new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, null);
    }

    public void enviarCorreoDeActivacion(Usuario usuario) {
        Properties propiedadesSesionEmail = new Properties();
        propiedadesSesionEmail.setProperty("mail.smtp.port", "2000");
        propiedadesSesionEmail.setProperty("mail.smtp.host", "localhost");
        Session sesionEmail = Session.getInstance(propiedadesSesionEmail);

        try {
            MimeMessage mensaje = new MimeMessage(sesionEmail);
            mensaje.setFrom(new InternetAddress("scpfc@scpfc.com"));
            mensaje.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(usuario.getCorreoElectronico(), usuario.getNombre()));
            mensaje.setSubject("SCPFC - Confirma tu cuenta");

            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletRequest request = (HttpServletRequest) context.getRequest();
            String url = request.getRequestURL().toString();
            String baseUrl = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath() + "/";

            String textoMensaje
                    = "Para confirmar tu cuenta ve a "
                    + baseUrl
                    + "/activar-cuenta"
                    + "?id=" + usuario.getId()
                    + "&codigo=" + usuario.getCodigoDeActivacion();
            mensaje.setText(textoMensaje);

            Transport.send(mensaje);
        } catch (MessagingException | java.io.UnsupportedEncodingException me) {

        }

        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Se ha enviado un correo de confirmación a "
                + usuario.getCorreoElectronico(),
                null);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);

    }

}
