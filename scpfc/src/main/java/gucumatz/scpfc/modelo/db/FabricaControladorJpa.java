
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gucumatz.scpfc.modelo.db;

import gucumatz.scpfc.modelo.db.PuestoJpaController;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author lchacon
 */
public class FabricaControladorJpa implements java.io.Serializable{

    private final EntityManagerFactory emf;

    public FabricaControladorJpa() {
        this.emf = Persistence.createEntityManagerFactory("SCPFC-PU");
    }

    public PuestoJpaController obtenerControladorJpaPuesto() {
        return new PuestoJpaController(emf);
    }
}