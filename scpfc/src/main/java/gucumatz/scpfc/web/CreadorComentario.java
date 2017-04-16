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
        
        //TEMP
        sesionActiva = new SesionActiva();
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
     * @return true si el comentairo es válido, false e.o.c.
     */
    private boolean comentarioValido() {
        
        if(this.comentario.equals(""))
            return false;
        else if(this.comentario.length() > 1024)
            return false;
        
        return true;
    }
    
    /**
     * Método encargado de crear el comentario.
     *
     * @return El nombre de la vista que va a responder.
     */
    public String agregarComentario() {
        FabricaControladorJpa fab = new FabricaControladorJpa();
        ComentarioJpaController jpaComentario = fab.obtenerControladorJpaComentario();
        
        // Obtener el puesto relacionado al ID
        PuestoJpaController jpaPuesto = fab.obtenerControladorJpaPuesto();
        Puesto p = jpaPuesto.findPuesto(this.puestoId);
        
        // Validación del puesto
        if (p == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Puesto no válido.", null);
            faceContext.addMessage(null, message);
            
            return "calycom";
        }
        
        // Crear el comentario
        Comentario c = new Comentario();

        // Validar el comentario, error si es necesario
        if(!comentarioValido()) {
            message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Comentario no válido", null);
            faceContext.addMessage(null, message);
            
            return "calycom";
        }

        c.setComentario(this.comentario);
        c.setPuestoId(p);
        Usuario u = sesionActiva.getUsuario();

        // Manejo de errores, no hay usuario válido
        if (u == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Solo los usuarios registrados pueden comentar.", null);
            faceContext.addMessage(null, message);
            
            return "calycom";
        }

        c.setUsuarioId(u);

        // Finalmente, la fecha
        Date fecha = new Date();
        c.setFecha(fecha);
        jpaComentario.create(c);
        
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Comentario registrado.", null);
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