/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gucumatz.scpfc.modelo.db;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author lchacon
 */
public class FabricaControladorJpa {

    private final EntityManagerFactory emf;

    public FabricaControladorJpa() {
        this.emf = Persistence.createEntityManagerFactory("SCPFC-PU");
    }

    public ComentarioJpaController obtenerControladorJpaComentario() {
        return new ComentarioJpaController(emf);
    }
    
    public CalificacionJpaController obtenerControladorJpaCalificacion() {
        return new CalificacionJpaController(emf);
    }
    
    public UsuarioJpaController obtenerControladorJpaUsuario() {
        return new UsuarioJpaController(emf);
    }
    
    public PuestoJpaController obtenerControladorJpaPuesto() {
        return new PuestoJpaController(emf);
    }
}
