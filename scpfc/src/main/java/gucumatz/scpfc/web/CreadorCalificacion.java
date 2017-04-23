/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gucumatz.scpfc.web;

import gucumatz.scpfc.controlador.VisorPuesto;
import gucumatz.scpfc.modelo.Calificacion;
import gucumatz.scpfc.modelo.Puesto;
import gucumatz.scpfc.modelo.Usuario;
import gucumatz.scpfc.modelo.db.CalificacionJpaController;
import gucumatz.scpfc.modelo.db.FabricaControladorJpa;
import gucumatz.scpfc.modelo.db.PuestoJpaController;
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
@ManagedBean(name="calBean")
@RequestScoped
public class CreadorCalificacion {

    private float calificacion;
    private final FacesContext faceContext; // Obtiene información de la aplicación
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

    public VisorPuesto getVisorPuesto() {
        return visorPuesto;
    }

    public void setVisorPuesto(VisorPuesto visorPuesto) {
        this.visorPuesto = visorPuesto;
    }

    /**
     * Metodo para validar los campos de la calificacion
     * @return true si la calificación es válida, false e.o.c.
     */
    private boolean esValida() {
        FabricaControladorJpa fab = new FabricaControladorJpa();
        CalificacionJpaController jpaCalificacion = fab.obtenerControladorJpaCalificacion();

        // Obtener el puesto relacionado al ID
        PuestoJpaController jpaPuesto = fab.obtenerControladorJpaPuesto();
        Puesto p = visorPuesto.getPuesto();

        // Validar puesto
        if (p == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: Puesto no registrado.", null);
            faceContext.addMessage(null, message);

            return false;
        }

        // Obtener usuario actual
        Usuario u = sesionActiva.getUsuario();

        // Validar al usuario
        if (u == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Debes iniciar sesión para calificar.", null);
            faceContext.addMessage(null, message);

            return false;
        }

        // Validar que el usuario no califique varias veces
        if (jpaCalificacion.findByUsuarioPuesto(u, p) != null) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: No se puede calificar más de una vez.", null);
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
        if (!esValida())
            return;

        // Crear la calificación
        FabricaControladorJpa fab = new FabricaControladorJpa();
        CalificacionJpaController jpaCalificacion = fab.obtenerControladorJpaCalificacion();
        Calificacion c = new Calificacion();
        c.setCalificacion(this.calificacion);

        // Obtener el puesto relacionado al ID
        PuestoJpaController jpaPuesto = fab.obtenerControladorJpaPuesto();
        Puesto p = visorPuesto.getPuesto();
        c.setPuestoId(p);
        visorPuesto.getCalificaciones().add(c);

        // Obtener usuario actual
        Usuario u = sesionActiva.getUsuario();
        c.setUsuarioId(u);

        jpaCalificacion.create(c);

        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Calificación registrada.", null);
        faceContext.addMessage(null, message);

        return;
    }

    public SesionActiva getSesionActiva() {
        return sesionActiva;
    }

    public void setSesionActiva(SesionActiva sesionActiva) {
        this.sesionActiva = sesionActiva;
    }
}
