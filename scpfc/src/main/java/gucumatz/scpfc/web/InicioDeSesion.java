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
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author lchacon
 */
@ManagedBean
@ViewScoped
public class InicioDeSesion implements Serializable {

    private static final String MENSAJE_CUENTA_NO_EXISTE
            = "El usuario no existe";
    private static final String MENSAJE_CUENTA_NO_CONFIRMADA
            = "Esta cuenta no ha sido confirmada";
    private static final String MENSAJE_CONTRASENA_INCORRECTA
            = "Contraseña incorrecta";

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
     * Bean de sesión para recordar el usuario que inicia de sesión.
     */
    @ManagedProperty("#{sesionActiva}")
    private SesionActiva sesionActiva;

    public InicioDeSesion() {
        jpaUsuario = new FabricaControladorJpa().obtenerControladorJpaUsuario();
    }

    /**
     * Inicia sesión con los datos actuales.
     *
     * @return La página que debe mostrar. null si hay errores o index si pudo
     * iniciar sesión.
     */
    public String iniciarSesion() {
        Usuario usuario = jpaUsuario.buscarUsuario(cuenta);
        sesionActiva.setUsuario(usuario);
        return "index?faces-redirect=true";
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
     * Verifica que la cuenta de usuario exista y esté confirmada.
     */
    public void validarCuenta(FacesContext context, UIComponent component, Object value)
            throws ValidatorException {
        String cuenta = (String) value;

        /* Si está vacío, el atributo required lo rechazará. */
        if (cuenta == null || cuenta.isEmpty()) {
            return;
        }

        /* Verifica que la cuenta exista. */
        Usuario usuario = jpaUsuario.buscarUsuario(cuenta);
        if (usuario == null) {
            throw new ValidatorException(crearMensajeDeError(MENSAJE_CUENTA_NO_EXISTE));
        }

        /* Verifica que la cuenta esté confirmada. */
        if (!usuario.getConfirmada()) {
            throw new ValidatorException(crearMensajeDeError(MENSAJE_CUENTA_NO_CONFIRMADA));
        }
    }

    /**
     * Verifica que la contraseña recibida coincida con la del usuario.
     */
    public void validarContrasena(FacesContext context, UIComponent component, Object value)
            throws ValidatorException {
        String contrasena = (String) value;

        /* Obtiene la componente con el nombre de usuario y lo extrae. */
        UIInput componenteCuenta = (UIInput) component.getAttributes().get("cuenta");
        String cuenta = (String) componenteCuenta.getValue();

        /* Si alguno está vacío, el atributo required lo rechazará. */
        if (contrasena == null || contrasena.isEmpty()
                || cuenta == null || cuenta.isEmpty()) {
            return;
        }

        /* Obtiene al usuario. Si el resultado es nulo, el validador de la cuenta lo rechazará. */
        Usuario usuario = jpaUsuario.buscarUsuario(cuenta);
        if (usuario == null) {
            return;
        }

        /* Verifica que la contraseña sea correcta. */
        if (!contrasena.equals(usuario.getContrasena())) {
            throw new ValidatorException(crearMensajeDeError(MENSAJE_CONTRASENA_INCORRECTA));
        }
    }

    /**
     * Crea un nuevo mensaje de error. El mensaje no contiene detalles y tiene
     * severidad de error.
     */
    private FacesMessage crearMensajeDeError(String mensaje) {
        return new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, null);
    }

}
