/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gucumatz.scpfc.web;

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
import javax.servlet.http.HttpServletRequest;

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
    private Long puestoId;
    private final HttpServletRequest httpServletRequest; // Obtiene información de todas las peticiones de usuario.
    private final FacesContext faceContext; // Obtiene información de la aplicación
    private FacesMessage message;
    
    @ManagedProperty("#{sesionActiva}")
    private SesionActiva sesionActiva;

    /**
     * Constructor para inicializar los valores de faceContext y
     * httpServletRequest.
     */
    public CreadorCalificacion() {
        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        
        //TEMP
        sesionActiva = new SesionActiva();
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
     * Obtiene el puesto asociado.
     *
     * @return El id del puesto.
     */
    public Long getPuestoId() {
        return puestoId;
    }

    /**
     * Establece el puesto asociado.
     *
     * @param puestoId El nuevo valor del puesto asociado.
     */
    public void setPuestoId(Long puestoId) {
        this.puestoId = puestoId;
    }

    /**
     * Método encargado de crear la calificación.
     *
     * @return El nombre de la vista que va a responder.
     */
    public String agregarCalificacion() {
        FabricaControladorJpa fab = new FabricaControladorJpa();
        CalificacionJpaController jpaCalificacion = fab.obtenerControladorJpaCalificacion();
        
        // Obtener el puesto relacionado al ID
        PuestoJpaController jpaPuesto = fab.obtenerControladorJpaPuesto();
        Puesto p = jpaPuesto.findPuesto(this.puestoId);
        
        // Crear la calificación
        Calificacion c = new Calificacion();
        c.setCalificacion(this.calificacion);
        c.setPuestoId(p);
        Usuario u = sesionActiva.getUsuario();
        
        // Manejo de errores, no hay usuario válido
        if (u == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Solo los usuarios registrados pueden calificar.", null);
            faceContext.addMessage(null, message);
            
            return "calycom";
        }
        
        c.setUsuarioId(u);
        
        jpaCalificacion.create(c);
        
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Calificación registrada.", null);
        faceContext.addMessage(null, message);

        return "calycom";
    }
    
    public SesionActiva getSesionActiva() {
        return sesionActiva;
    }

    public void setSesionActiva(SesionActiva sesionActiva) {
        this.sesionActiva = sesionActiva;
    }
}