package gucumatz.scpfc.controlador;

import gucumatz.scpfc.modelo.FotoPuesto;
import gucumatz.scpfc.modelo.Puesto;
import gucumatz.scpfc.modelo.db.ControladorJpaFotoPuesto;
import gucumatz.scpfc.modelo.db.ControladorJpaPuesto;
import gucumatz.scpfc.modelo.db.FabricaControladorJpa;
import gucumatz.scpfc.modelo.db.exceptions.NonexistentEntityException;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.primefaces.model.UploadedFile;
/**
 *
 * @author lchacon
 */
@ManagedBean
@ViewScoped
public class EditorPuesto implements Serializable {
    /**
     * Tamaño máximo para cada foto. El máximo es 4MB.
     */
    private static final int FOTO_TAM_MAX = 4 * 1024 * 1024;

    private static final String MENSAJE_NOMBRE_NO_DISPONIBLE
        = "Ya existe un puesto con este nombre";

    private final ControladorJpaPuesto jpaPuesto;
    private final ControladorJpaFotoPuesto jpaFotoPuesto;

    /**
     * ID del puesto a editar.
     */
    private Long idPuesto;

    /**
     * Puesto a editar.
     */
    private Puesto puesto;
    private UploadedFile foto;
    /**
     * Crea una nueva instancia de EditorPuesto
     */
    public EditorPuesto() {
        FabricaControladorJpa fabricaJpa = new FabricaControladorJpa();
        jpaPuesto = fabricaJpa.obtenerControladorJpaPuesto();
        jpaFotoPuesto = fabricaJpa.obtenerControladorJpaFotoPuesto();
    }

    public Long getIdPuesto() {
        return this.idPuesto;
    }

    public void setIdPuesto(Long idPuesto) {
        this.idPuesto = idPuesto;
    }

    public Puesto getPuesto() {
        return this.puesto;
    }

    /**
     * Metodo para obtener la foto.
     *
     * @return foto.
     */
    public UploadedFile getFoto() {
        return this.foto;
    }

    /**
     * Metodo para cambiar la foto.
     *
     * @param nuevo - nueva foto.
     */
    public void setFoto(UploadedFile nuevo) {
        this.foto = nuevo;
    }

    /**
     * Inicializa el Bean buscando el puesto con el ID recibido. Si no se
     * recibió un ID o el puesto no existe, redirige a la página principal.
     *
     * @return dirección a la que redirige, o null si no es necesario
     */
    public String preparar() {
        if (idPuesto != null) {
            puesto = jpaPuesto.buscarPorId(idPuesto);

            if (puesto != null) {
                /* Todo bien, permanece en la página. */
                return null;
            }
        }

        return "index?faces-redirect=true";
    }

    /**
     * Actualiza los datos del puesto.
     *
     */
    public void actualizar() {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        try {
            jpaPuesto.editar(puesto);
            FacesMessage facesMessage
                = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Datos actualizados!", null);
            facesContext.addMessage(null, facesMessage);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(EditorPuesto.class.getName()).log(Level.SEVERE,
                    null, ex);
        } catch (Exception ex) {
            Logger.getLogger(EditorPuesto.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
        redirecciona();
    }

    /**
     * Método que elimina una imagen del puesto.
     * @param id tipo Long: Id de la imagen a eliminar.
     */
    public void eliminaImagen(Long id) {
        try {
            FotoPuesto fotoElim = jpaFotoPuesto.findFotoPuesto(id);
            this.puesto.getFotosPuesto().remove(fotoElim);
            jpaFotoPuesto.destruir(fotoElim.getId());
            ExternalContext ec = FacesContext.getCurrentInstance()
                .getExternalContext();
            ec.redirect("editar-puesto.xhtml?id=" + this.puesto.getId());
        } catch (Exception ex) {
            Logger.getLogger(EditorPuesto.class.getName()).log(Level.SEVERE,
                    null, ex);
        }

    }

    /**
     * Método que sube una nueva imagen del puesto.
     */
    public void subeImagen() {
        try {
            if (this.foto != null) {
                guardaImagen(this.foto, this.puesto.getFotosPuesto());
            }
            ExternalContext ec = FacesContext.getCurrentInstance()
                .getExternalContext();
            ec.redirect("editar-puesto.xhtml?id=" + this.puesto.getId());
        } catch (IOException ex) {
            Logger.getLogger(EditorPuesto.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }

    /**
     * Comprueba que el nombre del puesto sea único.
     *
     * @param context FacesContext para la solicitud que se está procesando
     * @param component el componente que se está revisando
     * @param value el valor que se quiere validar
     * @throws ValidatorException si el valor dado no es válido
     */
    public void validarNombre(FacesContext context,
                              UIComponent component,
                              Object value)
        throws ValidatorException {
        String nombrePorValidar = (String) value;

        /* Si es vacío, el atributo required lo rechaza. */
        if (nombrePorValidar == null || nombrePorValidar.isEmpty()) {
            return;
        }

        /* Verifica que no exista otro puesto con el mismo nombre. */
        Puesto puesto2 = jpaPuesto.buscarPorNombre(nombrePorValidar);
        if (puesto2 != null && puesto2.getId() != puesto.getId()) {
            FacesMessage mensajeDeError
                = crearMensajeDeError(MENSAJE_NOMBRE_NO_DISPONIBLE);
            throw new ValidatorException(mensajeDeError);
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
     * Comprueba que el horario tenga el formato correcto
     *
     * @param context FacesContext para la solicitud que se está procesando
     * @param component el componente que se está revisando
     * @param value el valor que se quiere validar
     * @throws ValidatorException si el valor dado no es válido
     */
    public void validarHorario(FacesContext context,
                               UIComponent component,
                               Object value)
                                throws ValidatorException {
        String horario = (String) value;
        String hora1 = "2[0-3]";
        String hora2 = "1[0-9]";
        String hora3 = "0[0-9]";
        String hora = "(" + hora1 + "|" + hora2 + "|" + hora3 + ")";
        String minuto = "[0-5][0-9]";
        String regex = "Abierto\\sdesde\\slas\\s"
                        + hora
                        + ":"
                        + minuto
                        + "\\shrs\\.\\sa\\slas\\s"
                        + hora
                        + ":"
                        + minuto
                        + "\\shrs\\.";
        Pattern patron = Pattern.compile(regex);
        Matcher match = patron.matcher(horario);
        if (!match.matches()) {
            FacesMessage mensajeDeError = crearMensajeDeError(
                "Error de Formato <Abierto desde las HH:MM hrs."
                + " a las HH:MM hrs.>");
            throw new ValidatorException(mensajeDeError);
        }
    }

    /**
     * Metodo que guarda una imagen en la memoria del equipo y la base de datos.
     *
     * @param up - archivo a guardar.
     * @param fp - lista de fotos
     * @return Devuelve true si no hubo problemas al guardar los datos.
     */
    public boolean guardaImagen(UploadedFile up, List<FotoPuesto> fp) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (up == null) {
            return true;
        }
        try {
            String extensionFoto;
            String nombreDeArchivo2 = up.getFileName();
            if (nombreDeArchivo2.endsWith(".jpg")
                || nombreDeArchivo2.endsWith(".jpeg")) {
                extensionFoto = ".jpg";
            } else {
                extensionFoto = ".png";
            }
            ManejadorDeImagenes mdi = new ManejadorDeImagenes();
            FotoPuesto f = new FotoPuesto();
            mdi.escribirImagen(up, up.getFileName());
            f.setUrl("puesto/" + up.getFileName());
            f.setPuesto(puesto);
            jpaFotoPuesto.crear(f);
            fp.add(f);

        } catch (Exception e) {
            FacesMessage facesMessage
                = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "ERROR Al tratar de subir la foto "
                    + " " + e.getMessage(), null);
            facesContext.addMessage(null, facesMessage);
        }
        return false;
    }

    /**
    * Metodo para saber si una imagen ya existia en la base de datos
    * @param id de imagen
    * @return Devuelve la foto si existe
    */
    private FotoPuesto buscaImagen(int id) {
        for (FotoPuesto fp: puesto.getFotosPuesto()) {
            if (fp.getUrl().contains("-" + Integer.toString(id))) {
                return fp;
            }
        }
        return null;
    }
    /**
    * Metodo que valida el formato de una imagen
    * @param up - archivo
    * @return Devuelve true si el formato es correcto.
    */
    private boolean validaFormato(UploadedFile up) {
        if (up == null) {
            return true;
        }
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String nombreDeArchivo = up.getFileName();
        if (up.getSize() > FOTO_TAM_MAX) {
            FacesMessage facesMessage
                = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "ERROR de formato, tamaño excedido", null);
            facesContext.addMessage(null, facesMessage);
            return false;
        }


        if (nombreDeArchivo.endsWith(".jpg")
            || nombreDeArchivo.endsWith(".jpeg")
            || nombreDeArchivo.endsWith(".png")) {
            return true;
        }

        FacesMessage facesMessage
                = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "ERROR de formato, solo png o jpg", null);
        facesContext.addMessage(null, facesMessage);
        return false;
    }

    /**
     * Metodo que redirecciona la página actual a DetallesPuesto.xhtml.
     */
    public void redirecciona() {
        try {
            FacesContext.getCurrentInstance().getExternalContext()
                .redirect("./DetallesPuesto.xhtml?id=" + idPuesto);
        } catch (Exception e) {
            FacesMessage facesMessage
                = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "ERROR 01" + e.toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
    }
}
