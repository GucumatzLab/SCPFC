/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gucumatz.scpfc.web;

import gucumatz.scpfc.modelo.Comentario;
import gucumatz.scpfc.modelo.Puesto;
import gucumatz.scpfc.modelo.Usuario;
import gucumatz.scpfc.modelo.db.ComentarioJpaController;
import gucumatz.scpfc.modelo.db.FabricaControladorJpa;
import gucumatz.scpfc.modelo.db.PuestoJpaController;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * ManagedBean para el comentario de un puesto
 *
 * @author Jaz
 */
@ManagedBean(name="comBean")
@RequestScoped
public class CreadorComentario {

    private String comentario;
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
    public CreadorComentario() {
        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
    }

    /**
     * Obtiene el comentario.
     *
     * @return El comentario.
     */
    public String getComentario() {
        return comentario;
    }

    /**
     * Establece el comentario.
     *
     * @param comentario El nuevo valor del comentario.
     */
    public void setComentario(String comentario) {
        this.comentario = comentario;
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
     * Método interno para comprobar la validez del comentario
     * @return true si el comentario es válido, false e.o.c.
     */
    private boolean esValido() {
        
        // Validar comentario
        if(this.comentario.equals("")) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: El comentario debe contener texto.", null);
            faceContext.addMessage(null, message);
            
            return false;
        } else if(this.comentario.length() > 1024) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: Comentario demasiado largo.", null);
            faceContext.addMessage(null, message);
            
            return false;
        }
        
        // Para validar al puesto y al usuario
        FabricaControladorJpa fab = new FabricaControladorJpa();
        
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
        
        return true;
    }
    
    /**
     * Método encargado de crear el comentario.
     *
     * @return El nombre de la vista que va a responder.
     */
    public String agregarComentario() {
        
        // Revisar que sea válido el comentario
        if (!esValido())
            return null;
        
        FabricaControladorJpa fab = new FabricaControladorJpa();
        ComentarioJpaController jpaComentario = fab.obtenerControladorJpaComentario();
        
        // Crear el comentario
        Comentario c = new Comentario();
        c.setComentario(this.comentario);
        
        // Obtener el puesto relacionado al ID
        PuestoJpaController jpaPuesto = fab.obtenerControladorJpaPuesto();
        Puesto p = jpaPuesto.findPuesto(this.puestoId);
        c.setPuestoId(p);
        
        // Obtener usuario actual
        Usuario u = sesionActiva.getUsuario();
        c.setUsuarioId(u);

        // Finalmente, la fecha
        Date fecha = new Date();
        c.setFecha(fecha);
        jpaComentario.create(c);
        
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Comentario registrado.", null);
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