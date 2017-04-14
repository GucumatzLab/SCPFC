/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gucumatz.scpfc.web;

import gucumatz.scpfc.modelo.Usuario;
import gucumatz.scpfc.modelo.db.FabricaControladorJpa;
import gucumatz.scpfc.modelo.db.UsuarioJpaController;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author lchacon
 */
@ManagedBean
@SessionScoped
public class SesionActiva implements Serializable {

    /**
     * Usuario correspondiente a esta sesión.
     */
    private Usuario usuario;

    /**
     * Creates a new instance of SesionIH
     */
    public SesionActiva() {
        // TEMPORAL
        FabricaControladorJpa fab = new FabricaControladorJpa();
        UsuarioJpaController jpaUsuario = fab.obtenerControladorJpaUsuario();
        usuario = jpaUsuario.findUsuario(new Long(1));
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void cerrarSesion() {
        usuario = null;
    }

    public Usuario obtenerUsuarioActual() {
        return usuario;
    }
}
