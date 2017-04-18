/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gucumatz.scpfc.web;

import gucumatz.scpfc.modelo.Usuario;
import java.io.IOException;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.StreamedContent;

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

    public SesionActiva() {
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

    /**
     * Regresa la foto del usuario actual, o una imagen por omisión si no tiene
     * o no ha iniciado sesión.
     */
    public StreamedContent getFotoUsuarioActual() {
        try {
            return new ManejadorDeImagenes().getFotoDeUsuario(usuario);
        } catch (IOException ioe) {
            return null;
        }
    }

    /**
     * Dice si se ha iniciado sesión
     */
    public boolean getHaySesionActiva() {
        return usuario != null;
    }

    /**
     * Dice si el usuario actual es un administrador.
     */
    public boolean getEsAdministrador() {
        return usuario != null && usuario.getEsAdministrador();
    }

    /**
     * Redirige a la página principal.
     */
    public String redirigeAPaginaPrincipal() {
        return "index?faces-redirect=true";
    }

}
