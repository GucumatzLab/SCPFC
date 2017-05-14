/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gucumatz.scpfc.controlador;

import gucumatz.scpfc.controlador.VisorPuesto;
import gucumatz.scpfc.modelo.Comentario;
import gucumatz.scpfc.modelo.Puesto;
import gucumatz.scpfc.modelo.Usuario;
import gucumatz.scpfc.modelo.db.ControladorJpaComentario;
import gucumatz.scpfc.modelo.db.FabricaControladorJpa;
import gucumatz.scpfc.modelo.db.ControladorJpaPuesto;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

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
    public CreadorComentario() {
        faceContext = FacesContext.getCurrentInstance();
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
        this.comentario = comentario.trim();
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
        ControladorJpaPuesto jpaPuesto = fab.obtenerControladorJpaPuesto();
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

        return true;
    }

    /**
     * Método encargado de crear el comentario.
     */
    public void agregarComentario() {
        // Revisar que sea válido el comentario
        if (!esValido())
            return;

        FabricaControladorJpa fab = new FabricaControladorJpa();
        ControladorJpaComentario jpaComentario = fab.obtenerControladorJpaComentario();

        // Crear el comentario
        Comentario c = new Comentario();
        c.setComentario(this.comentario);

        // Obtener el puesto relacionado al ID
        ControladorJpaPuesto jpaPuesto = fab.obtenerControladorJpaPuesto();
        Puesto p = visorPuesto.getPuesto();
        c.setPuesto(p);
        visorPuesto.getPuesto().getComentarios().add(c);

        // Obtener usuario actual
        Usuario u = sesionActiva.getUsuario();
        c.setUsuario(u);

        // Finalmente, la fecha
        Date fecha = new Date();
        c.setFecha(fecha);
        jpaComentario.crear(c);

        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Comentario registrado.", null);
        faceContext.addMessage(null, message);

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
