package gucumatz.scpfc.controlador;

import gucumatz.scpfc.modelo.Calificacion;
import gucumatz.scpfc.modelo.Puesto;
import gucumatz.scpfc.modelo.Usuario;
import gucumatz.scpfc.modelo.db.ControladorJpaCalificacion;
import gucumatz.scpfc.modelo.db.ControladorJpaPuesto;
import gucumatz.scpfc.modelo.db.FabricaControladorJpa;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

/**
 *
 * ManagedBean para la calificación de un puesto
 *
 * @author Jaz
 */
@ManagedBean (name = "calBean")
@RequestScoped
public class CreadorCalificacion {

    private float calificacion;
    // Obtiene información de la aplicación
    private final FacesContext faceContext;
    private FacesMessage message;

    @ManagedProperty("#{sesionActiva}")
    private SesionActiva sesionActiva;

    @ManagedProperty("#{visorPuesto}")
    private VisorPuesto visorPuesto;

    /**
     * Constructor para inicializar los valores de faceContext y
     * httpServletRequest.
     */
    public CreadorCalificacion() {
        faceContext = FacesContext.getCurrentInstance();
    }

    /**
     * Inicializa el bean. Busca si el usuario actual ya calificó al puesto que
     * se está viendo y, si lo hizo, recupera la calificación anterior.
     */
    @PostConstruct
    public void initCal() {

        // Ver si hay usuario
        Usuario u = sesionActiva.getUsuario();

        if (u == null) {
            return;
        }

        // Obtener puesto, ver si hay calificación previa
        Puesto p = visorPuesto.getPuesto();
        ControladorJpaCalificacion jpaCalificacion
            = new FabricaControladorJpa().obtenerControladorJpaCalificacion();
        Calificacion prev = jpaCalificacion.buscarPorUsuarioYPuesto(u, p);

        if (prev != null) {
            this.calificacion = prev.getCalificacion();
        }
    }

    /**
     * Obtiene la calificación.
     *
     * @return La calificación.
     */
    public int getCalificacion() {
        return (int) calificacion;
    }

    /**
     * Establece la calificación.
     *
     * @param calificacion El nuevo valor de la calificación.
     */
    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    /**
     * Obtiene el objeto relacionado a la vista del puesto.
     *
     * @return el <code>VisorPuesto</code> ligado.
     */
    public VisorPuesto getVisorPuesto() {
        return visorPuesto;
    }

    /**
     * Actualiza el objeto relacionado a la vista del puesto.
     *
     * @param visorPuesto el nuevo <code>VisorPuesto</code>.
     */
    public void setVisorPuesto(VisorPuesto visorPuesto) {
        this.visorPuesto = visorPuesto;
    }

    /**
     * Metodo para validar los campos de la calificacion
     * @return true si la calificación es válida, false e.o.c.
     */
    private boolean esValida() {
        FabricaControladorJpa fab = new FabricaControladorJpa();
        ControladorJpaCalificacion jpaCalificacion
            = fab.obtenerControladorJpaCalificacion();

        // Obtener el puesto relacionado al ID
        ControladorJpaPuesto jpaPuesto = fab.obtenerControladorJpaPuesto();
        Puesto p = visorPuesto.getPuesto();

        // Validar puesto
        if (p == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Error: Puesto no registrado.", null);
            faceContext.addMessage(null, message);

            return false;
        }

        // Obtener usuario actual
        Usuario u = sesionActiva.getUsuario();

        // Validar al usuario
        if (u == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_WARN,
                "Debes iniciar sesión para calificar.", null);
            faceContext.addMessage(null, message);

            return false;
        }

        return true;
    }

    /**
     * Método encargado de crear la calificación.
     */
    public void agregarCalificacion() {

        // Revisar que no haya errores
        if (!esValida()) {
            return;
        }

        // Crear la calificación
        FabricaControladorJpa fab = new FabricaControladorJpa();
        ControladorJpaCalificacion jpaCalificacion
            = fab.obtenerControladorJpaCalificacion();

        // Obtener el puesto relacionado al ID
        Puesto p = visorPuesto.getPuesto();

        // Obtener usuario actual
        Usuario u = sesionActiva.getUsuario();

        // Revisar si debemos crear o editar
        // Caso 1: Editar
        Calificacion prev = jpaCalificacion.buscarPorUsuarioYPuesto(u, p);

        if (prev != null) {
            try {
                visorPuesto.getPuesto().getCalificaciones().remove(prev);

                prev.setCalificacion(this.calificacion);
                jpaCalificacion.editar(prev);

                visorPuesto.getPuesto().getCalificaciones().add(prev);

                message = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Calificación actualizada.", null);
                faceContext.addMessage(null, message);
            } catch (Exception e) {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error: La calificación ya no existe.", null);
                faceContext.addMessage(null, message);

                return;
            }

        // Caso 2: Crear
        } else {
            Calificacion c = new Calificacion();
            c.setPuesto(p);
            c.setUsuario(u);
            c.setCalificacion(this.calificacion);

            jpaCalificacion.crear(c);

            visorPuesto.getPuesto().getCalificaciones().add(c);

            message = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Calificación registrada.", null);
            faceContext.addMessage(null, message);
        }

        return;
    }

    /**
     * Regresa la sesión activa actual.
     *
     * @return El <code>SesionActiva</code> actual.
     */
    public SesionActiva getSesionActiva() {
        return sesionActiva;
    }

    /**
     * Actualiza la sesión activa actual.
     *
     * @param sesionActiva El nuevo objeto <code>SesionActiva</code> actual.
     */
    public void setSesionActiva(SesionActiva sesionActiva) {
        this.sesionActiva = sesionActiva;
    }
}
