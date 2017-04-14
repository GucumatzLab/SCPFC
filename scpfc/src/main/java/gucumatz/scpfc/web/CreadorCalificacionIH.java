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
import gucumatz.scpfc.modelo.Puesto;
import gucumatz.scpfc.modelo.Usuario;

/**
 *
 * ManagedBean para la calificación de un puesto
 *
 * @author Jaz
 */
@ManagedBean(name="calBean")
@RequestScoped
public class CreadorCalificacionIH {

    private float cal;
    private Long puestoId;
    private final HttpServletRequest httpServletRequest; // Obtiene información de todas las peticiones de usuario.
    private final FacesContext faceContext; // Obtiene información de la aplicación
    private FacesMessage message;

    /**
     * Constructor para inicializar los valores de faceContext y
     * httpServletRequest.
     */
    public CreadorCalificacionIH() {
        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        
        //TEMP
        puestoId = new Long(0);
    }

    /**
     * Obtiene la calificación.
     *
     * @return La calificación.
     */
    public float getCal() {
        return cal;
    }

    /**
     * Establece la calificación.
     *
     * @param cal El nuevo valor de la calificación.
     */
    public void setCal(float cal) {
        this.cal = cal;
    }
    
    /**
     * Obtiene el puesto asociadoa.
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
        CreadorCalificacion c = new CreadorCalificacion(cal, puestoId);
        
        boolean huboError = c.agregarCalificacion();
        
        if(huboError) {
            System.out.println("wjat");
            message
                = new FacesMessage(FacesMessage.SEVERITY_ERROR, "El usuario no existe", null);
            faceContext.addMessage(null, message);
            return "";
        }
        
        return "calificar";
    }
}