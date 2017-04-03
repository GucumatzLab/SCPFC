/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gucumatz.scpfc.web;

import gucumatz.scpfc.modelo.Usuario;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void cerrarSesion() {
        HttpSession httpSession = obtenerSesionActual();
        httpSession.removeAttribute("idUsuario");
        usuario = null;
    }

//    public Usuario obtenerUsuarioActual() {
//        HttpSession httpSession = obtenerSesionActual();
//        Object idUsuarioObj = httpSession.getAttribute("idUsuario");
//        if (idUsuarioObj == null || !(idUsuarioObj instanceof Long)) {
//            return null;
//        }
//        Long idUsuario = (Long) idUsuarioObj;
//
//        UsuarioJpaController jpaUsuario
//                = new FabricaControladorJpa().obtenerControladorJpaUsuario();
//        return jpaUsuario.findUsuario(idUsuario);
//    }
    public HttpSession obtenerSesionActual() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest httpServletRequest
                = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        HttpSession httpSession = httpServletRequest.getSession();
        return httpSession;
    }
}
