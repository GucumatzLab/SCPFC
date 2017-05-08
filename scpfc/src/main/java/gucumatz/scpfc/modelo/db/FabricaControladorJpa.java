/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gucumatz.scpfc.modelo.db;

import java.io.Serializable;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author lchacon
 */
public class FabricaControladorJpa implements Serializable {

    private final EntityManagerFactory emf;

    public FabricaControladorJpa() {
        this.emf = Persistence.createEntityManagerFactory("SCPFC-PU");
    }

    public ControladorJpaComentario obtenerControladorJpaComentario() {
        return new ControladorJpaComentario(emf);
    }
    
    public ControladorJpaCalificacion obtenerControladorJpaCalificacion() {
        return new ControladorJpaCalificacion(emf);
    }
    
    public ControladorJpaUsuario obtenerControladorJpaUsuario() {
        return new ControladorJpaUsuario(emf);
    }
    
    public ControladorJpaPuesto obtenerControladorJpaPuesto() {
        return new ControladorJpaPuesto(emf);
    }
    public ControladorJpaFotoPuesto obtenerControladorJpaFotoPuesto(){
        return new ControladorJpaFotoPuesto(emf);
    }
}
