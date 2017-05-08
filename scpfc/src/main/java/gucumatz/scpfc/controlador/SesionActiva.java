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

    /**
     * La página anterior. Sólo se usa para regresar después de iniciar
     * o cerrar sesión.
     */
    private String paginaAnterior;

    /**
     * El parámetro ID de la página anterior. Sólo se usa para recordar
     * qué puesto se estaba viendo si la página anterior es
     * DetallesPuesto. Tal vez exista una solución mejor.
     */
    private String paginaAnteriorId;

    public SesionActiva() {
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getPaginaAnterior() {
        return paginaAnterior;
    }

    public void setPaginaAnterior(String paginaAnterior) {
        this.paginaAnterior = paginaAnterior;
    }

    public String getPaginaAnteriorId() {
        return paginaAnteriorId;
    }

    public void setPaginaAnteriorId(String paginaAnteriorId) {
        this.paginaAnteriorId = paginaAnteriorId;
    }

    public String cerrarSesion() {
        usuario = null;
        return obtenerPaginaAnterior();
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

    /**
     * Obtiene la dirección de la página anterior y olvida cuál era
     * (establece el atributo en null). En caso que no haya ninguna
     * registrada, regresa "index".
     */
    public String obtenerPaginaAnterior() {
        String pagina = "index";
        if (paginaAnterior != null) {
            pagina = paginaAnterior;
            paginaAnterior = null;
        }

        pagina += "?faces-redirect=true";

        if (paginaAnteriorId != null && !paginaAnteriorId.isEmpty()) {
            pagina += "&id=" + paginaAnteriorId;
            paginaAnteriorId = null;
        }

        return pagina;
    }

    /**
     * Regresa el identificador de la vista para iniciar sesión. Sólo
     * existe para poder ajustar el valor de la página anterior antes
     * de ir a la página.
     */
    public String irAIniciarSesion() {
        return "iniciar-sesion?faces-redirect=true";
    }

}
