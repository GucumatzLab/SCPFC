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
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author lchacon
 */
@ManagedBean
@ViewScoped
public class InicioDeSesion implements Serializable {

    private final UsuarioJpaController jpaUsuario;

    /**
     * La cuenta con la que se quiere iniciar sesión. Puede ser un nombre de
     * usuario o una dirección de correo.
     */
    private String cuenta;

    /**
     * Contraseña usada para iniciar sesión.
     */
    private String contrasena;

    /**
     * Contexto para mostrar mensajes al usuario.
     */
    private FacesContext facesContext;

    /**
     * Bandera que indica si hay errores en los datos proporcionados. Cuando es
     * TRUE, no se debe permitir iniciar sesión.
     */
    private boolean hayErrores;

    private Usuario usuario;

    @ManagedProperty("#{sesionActiva}")
    private SesionActiva sesionActiva;

    /**
     * Creates a new instance of InicioDeSesionIH
     */
    public InicioDeSesion() {
        jpaUsuario = new FabricaControladorJpa().obtenerControladorJpaUsuario();
        hayErrores = false;
    }

    public void validarDatos() {
        facesContext = FacesContext.getCurrentInstance();

        usuario = buscarUsuario(cuenta);
        if (usuario == null) {
            FacesMessage mensaje
                    = new FacesMessage(FacesMessage.SEVERITY_ERROR, "El usuario no existe", null);
            //facesContext.addMessage(null, mensaje);
            hayErrores = true;
            return;
        }

        if (!comprobarContrasenaCorrecta(usuario)) {
            FacesMessage mensaje
                    = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Contraseña incorrecta", null);
            //facesContext.addMessage(null, mensaje);
            hayErrores = true;
            return;
        }

        hayErrores = false;
    }

    /**
     * Inicia sesión.
     *
     * @return La página que debe mostrar.
     */
    public String iniciarSesion() {
        /* Primero valida que los datos sean correctos. */
        validarDatos();

        if (hayErrores) {
            return null;
        }

        HttpSession httpSession = sesionActiva.obtenerSesionActual();
        if (httpSession.getAttribute("idUsuario") != null) {
            return null;
        }
        httpSession.setAttribute("idUsuario", usuario.getId());
        sesionActiva.setUsuario(usuario);
        return null;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public SesionActiva getSesionActiva() {
        return sesionActiva;
    }

    public void setSesionActiva(SesionActiva sesionActiva) {
        this.sesionActiva = sesionActiva;
    }

    /**
     * Comprueba que la contraseña recibida coincida con la del usuario dado.
     *
     * @param usuario el usuario contra el que se comprobará la contraseña
     * @return TRUE si las contraseñas coinciden, y FALSE en otro caso.
     */
    private boolean comprobarContrasenaCorrecta(Usuario usuario) {
        return usuario.getContrasena().equals(contrasena);
    }

    /**
     * Busca un usuario por nombre o correo electrónico.
     *
     * @param cuenta nombre de usuario o correo electrónico a buscar.
     * @return el usuario cuyo nombre o correo electrónico es `cuenta`, o null
     * si no existe.
     */
    private Usuario buscarUsuario(String cuenta) {
        Usuario u = jpaUsuario.findByNombre(cuenta);
        if (u != null) {
            return u;
        }

        return jpaUsuario.findByCorreoElectronico(cuenta);
    }

}
