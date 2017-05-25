package gucumatz.scpfc.controlador;

import gucumatz.scpfc.modelo.Puesto;
import gucumatz.scpfc.modelo.db.ControladorJpaFotoPuesto;
import gucumatz.scpfc.modelo.db.ControladorJpaPuesto;
import gucumatz.scpfc.modelo.db.FabricaControladorJpa;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author lchacon
 */
@ManagedBean
@ViewScoped
public class EditorPuesto implements Serializable {

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
     * @return la página a la que redirige
     */
    public String actualizar() {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        try {
            jpaPuesto.editar(puesto);
        } catch (Exception e) {
            /* Esto no debería ocurrir. */
            FacesMessage facesMessage
                = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Ocurrió un error al intentar actualizar el puesto", null);
            facesContext.addMessage(null, facesMessage);

            return null;
        }
        return "DetallesPuesto?faces-redirect=true&includeViewParams=true";
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

}
