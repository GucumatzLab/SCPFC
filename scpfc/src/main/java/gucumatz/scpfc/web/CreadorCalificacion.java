/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gucumatz.scpfc.web;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import gucumatz.scpfc.modelo.db.PuestoJpaController;
import gucumatz.scpfc.modelo.db.CalificacionJpaController;
import gucumatz.scpfc.modelo.db.FabricaControladorJpa;
import gucumatz.scpfc.modelo.Calificacion;
import gucumatz.scpfc.modelo.Puesto;
import gucumatz.scpfc.modelo.Usuario;

/**
 *
 * @author Jaz
 */
@ManagedBean
@ViewScoped
public class CreadorCalificacion {
    
    private float calificacion;
    private long puestoId;
    
    @ManagedProperty("#{sesionActiva}")
    private SesionActiva sesionActiva;
    
    /**
     * Creador simple que toma la calificación como <code>float</code> y el ID
     * del puesto asociado
     * @param f La calificacion en escala de 0.0 a 5.0
     */
    CreadorCalificacion(float f, long puestoId) {
        this.calificacion = f;
        this.puestoId = puestoId;
        sesionActiva = new SesionActiva();
    }
    
    /**
     * Método que toma los atributos del objeto para añadirlos a la BD
     * @return true si hubo error, false e.o.c.
     */
    public boolean agregarCalificacion() {
        FabricaControladorJpa fab = new FabricaControladorJpa();
        CalificacionJpaController jpaCalificacion = fab.obtenerControladorJpaCalificacion();
        
        // Obtener el puesto relacionado al ID
        PuestoJpaController jpaPuesto = fab.obtenerControladorJpaPuesto();
        Puesto p = jpaPuesto.findPuesto(this.puestoId);
        
        // Crear la calificación
        Calificacion c = new Calificacion(new Long(1));
        c.setCalificacion(this.calificacion);
        c.setPuestoId(p);
        Usuario u = sesionActiva.getUsuario();
        
        if (u == null)
            return true;
        
        c.setUsuarioId(u);
        
        jpaCalificacion.create(c);
        
        return false;
    }
    
    public SesionActiva getSesionActiva() {
        return sesionActiva;
    }

    public void setSesionActiva(SesionActiva sesionActiva) {
        this.sesionActiva = sesionActiva;
    }
    
    public CreadorCalificacion() {
    
    }
}
