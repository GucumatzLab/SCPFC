/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gucumatz.scpfc.web;

import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import gucumatz.scpfc.modelo.db.PuestoJpaController;
import gucumatz.scpfc.modelo.db.ComentarioJpaController;
import gucumatz.scpfc.modelo.db.FabricaControladorJpa;
import gucumatz.scpfc.modelo.Comentario;
import gucumatz.scpfc.modelo.Puesto;
import gucumatz.scpfc.modelo.Usuario;

/**
 *
 * @author Jaz
 */
@ManagedBean
@ViewScoped
public class CreadorComentario {
    
    private String comentario;
    private Long puestoId;
    
    @ManagedProperty("#{sesionActiva}")
    private SesionActiva sesionActiva;
    
    /**
     * Creador simple que toma el comentario en <code>String</code> y el ID
     * del puesto asociado.
     * @param f La comentario en escala de 0.0 a 5.0
     */
    CreadorComentario(String s, long puestoId) {
        this.comentario = s;
        this.puestoId = puestoId;
        sesionActiva = new SesionActiva();
    }
    
    private boolean comentarioValido() {
        
        if(this.comentario.equals(""))
            return false;
        else if(this.comentario.length() > 1024)
            return false;
        
        return true;
    }
    
    /**
     * Método que toma los atributos del objeto para añadirlos a la BD
     * @return true si hubo error, false e.o.c.
     */
    public boolean agregarComentario() {
        FabricaControladorJpa fab = new FabricaControladorJpa();
        ComentarioJpaController jpaComentario = fab.obtenerControladorJpaComentario();
        
        // Obtener el puesto relacionado al ID
        PuestoJpaController jpaPuesto = fab.obtenerControladorJpaPuesto();
        Puesto p = jpaPuesto.findPuesto(this.puestoId);
        
        // Crear el comentario
        Comentario c = new Comentario(new Long(1));
        
        // Validar el text
        if(!comentarioValido())
            return true;
        
        c.setComentario(this.comentario);
        c.setPuestoId(p);
        Usuario u = sesionActiva.getUsuario();
        
        // Validar al usuario
        if (u == null)
            return true;
        
        c.setUsuarioId(u);
        
        // Finalmente, la fecha
        Date fecha = new Date();
        c.setFecha(fecha);
        
        jpaComentario.create(c);
        
        return false;
    }
    
    public SesionActiva getSesionActiva() {
        return sesionActiva;
    }

    public void setSesionActiva(SesionActiva sesionActiva) {
        this.sesionActiva = sesionActiva;
    }
    
    public CreadorComentario() {
    
    }
}
