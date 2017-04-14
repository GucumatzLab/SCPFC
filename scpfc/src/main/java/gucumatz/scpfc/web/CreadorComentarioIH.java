/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gucumatz.scpfc.web;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * ManagedBean para la calificación de un puesto
 *
 * @author Jaz
 */
@ManagedBean(name="comBean")
@RequestScoped
public class CreadorComentarioIH {

    private String com;
    private Long puestoId;
    private final HttpServletRequest httpServletRequest; // Obtiene información de todas las peticiones de usuario.
    private final FacesContext faceContext; // Obtiene información de la aplicación
    private FacesMessage message;

    /**
     * Constructor para inicializar los valores de faceContext y
     * httpServletRequest.
     */
    public CreadorComentarioIH() {
        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        
        //TEMP
        puestoId = new Long(1);
    }

    /**
     * Obtiene el comentario.
     *
     * @return El comentario.
     */
    public String getCom() {
        return com;
    }

    /**
     * Establece el comentario.
     *
     * @param com El nuevo valor del comentario.
     */
    public void setCom(String com) {
        this.com = com;
    }
    
    /**
     * Obtiene el puesto asociado.
     *
     * @return El id del puesto.
     */
    public float getPuestoId() {
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
    public String agregar() {
        CreadorComentario c = new CreadorComentario(com, puestoId);
        
        boolean huboError = c.agregarComentario();
        
        if(huboError) {
            message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error.", null);
            faceContext.addMessage(null, message);
            return "comentar";
        }
        
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Comentario registrado.", null);
        faceContext.addMessage(null, message);
        return "comentar";
    }
}