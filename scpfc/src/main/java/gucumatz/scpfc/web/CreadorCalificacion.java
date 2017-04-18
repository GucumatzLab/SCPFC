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
     * Metodo para validar los campos de la calificacion
     * @return true si la calificación es válida, false e.o.c.
     */
    private boolean esValida() {
        FabricaControladorJpa fab = new FabricaControladorJpa();
        CalificacionJpaController jpaCalificacion = fab.obtenerControladorJpaCalificacion();
        
        // Obtener el puesto relacionado al ID
        PuestoJpaController jpaPuesto = fab.obtenerControladorJpaPuesto();
        Puesto p = jpaPuesto.findPuesto(this.puestoId);
        
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
     *
     * @return El nombre de la vista que va a responder.
     */
    public String agregarCalificacion() {
        
        // Revisar que no haya errores
        if (!esValida())
            return null;
        
        // Crear la calificación
        FabricaControladorJpa fab = new FabricaControladorJpa();
        CalificacionJpaController jpaCalificacion = fab.obtenerControladorJpaCalificacion();
        Calificacion c = new Calificacion();
        c.setCalificacion(this.calificacion);
        
        // Obtener el puesto relacionado al ID
        PuestoJpaController jpaPuesto = fab.obtenerControladorJpaPuesto();
        Puesto p = jpaPuesto.findPuesto(this.puestoId);
        c.setPuestoId(p);
        
        // Obtener usuario actual
        Usuario u = sesionActiva.getUsuario();
        c.setUsuarioId(u);
        
        jpaCalificacion.create(c);
        
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Calificación registrada.", null);
        faceContext.addMessage(null, message);

        return null;
    }
    
    public SesionActiva getSesionActiva() {
        return sesionActiva;
    }

    public void setSesionActiva(SesionActiva sesionActiva) {
        this.sesionActiva = sesionActiva;
    }
}