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

/**
 *
 * @author lchacon
 */
@ManagedBean
@ViewScoped
public class InicioDeSesion implements Serializable {

    /**
     * Controlador de JPA para buscar al usuario en la BD.
     */
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
     * Bandera que indica si hay errores en los datos proporcionados. Cuando es
     * TRUE, no se debe permitir iniciar sesión.
     */
    private boolean hayErrores;

    /**
     * Usuario con el que se quiere iniciar sesión.
     */
    private Usuario usuario;

    /**
     * Bean de sesión para recordar el usuario que inicia de sesión.
     */
    @ManagedProperty("#{sesionActiva}")
    private SesionActiva sesionActiva;

    public InicioDeSesion() {
        jpaUsuario = new FabricaControladorJpa().obtenerControladorJpaUsuario();
        hayErrores = false;
    }

    /**
     * Valida que los datos sean correctos y muestra mensajes de error.
     */
    public void validarDatos() {
        hayErrores = false;

        validarCuentaExiste();
        validarCuentaConfirmada();
        validarContrasenaCorrecta();
    }

    /**
     * Inicia sesión con los datos actuales.
     *
     * @return La página que debe mostrar. null si hay errores o index si pudo
     * iniciar sesión.
     */
    public String iniciarSesion() {
        /* Primero valida que los datos sean correctos. */
        validarDatos();

        /* Si hay errores, permanece en la misma página. */
        if (hayErrores) {
            return null;
        }

        /* Si no hay errores, actualiza la sesión. */
        sesionActiva.setUsuario(usuario);
        return "index";
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
     * Comprueba que la cuenta de usuario exista. Si existe, actualiza el valor
     * del atributo usuario.
     */
    private void validarCuentaExiste() {
        usuario = jpaUsuario.buscarUsuario(cuenta);

        if (usuario == null) {
            agregarError("El usuario no existe", "cuenta");
        }
    }

    /**
     * Comprueba que la cuenta con la que se quiere inicar sesión esté activada.
     */
    private void validarCuentaConfirmada() {
        if (usuario != null && !usuario.getConfirmada()) {
            agregarError("Esta cuenta no ha sido confirmada.", "cuenta");
        }
    }

    /**
     * Comprueba que la contraseña recibida coincida con la del usuario dado.
     *
     * @param usuario el usuario contra el que se comprobará la contraseña
     */
    private void validarContrasenaCorrecta() {
        if (usuario != null
                && !usuario.getContrasena().equals(contrasena)) {
            agregarError("Contraseña incorrecta", "contrasena");
        }
    }

    private void agregarError(String mensaje, String elemento) {
        hayErrores = true;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage facesMessage
                = new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, null);
        facesContext.addMessage("formulario:" + elemento, facesMessage);
    }

}
