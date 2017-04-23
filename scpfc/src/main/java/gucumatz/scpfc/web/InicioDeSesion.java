/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gucumatz.scpfc.web;

import gucumatz.scpfc.modelo.Usuario;
import gucumatz.scpfc.modelo.db.FabricaControladorJpa;
import gucumatz.scpfc.modelo.db.UsuarioJpaController;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.mail.MessagingException;

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

    /**
     * La página a la que hay que ir luego de iniciar sesión.
     */
    private String paginaAnterior;

    /**
     * Bandera que indica si el usuario no ha sido confirmado.
     */
    private boolean sinConfirmar;

    public InicioDeSesion() {
        jpaUsuario = new FabricaControladorJpa().obtenerControladorJpaUsuario();
    }

    /**
     * Obtiene la página anterior desde sesionActiva. Obtenerlo aquí
     * hace que sesionActiva lo olvide en cuanto se entra a la página
     * de iniciar sesión.
     */
    @PostConstruct
    private void init() {
        paginaAnterior = sesionActiva.obtenerPaginaAnterior();
    }

    /**
     * Inicia sesión con los datos actuales.
     *
     * @return La página que debe mostrar. null si hay errores o index si pudo
     * iniciar sesión.
     */
    public String iniciarSesion() {
        Usuario usuario = jpaUsuario.buscarUsuario(cuenta);
        /* No hace nada si no está confirmada. */
        if (!usuario.getConfirmada()) {
            return null;
        }

        sesionActiva.setUsuario(usuario);
        return paginaAnterior;
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

    public boolean getSinConfirmar() {
        return sinConfirmar;
    }

    /**
     * Verifica que la cuenta de usuario exista y esté confirmada.
     */
    public void validarCuenta(FacesContext context, UIComponent component, Object value)
            throws ValidatorException {
        sinConfirmar = false;
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
            /* No arrojamos una excepción para que este valor sea
             * marcado como válido y podamos usarlo al reenviar el
             * correo de confirmación. */
            sinConfirmar = true;
            context.addMessage(component.getClientId(),
                    crearMensajeDeError(MENSAJE_CUENTA_NO_CONFIRMADA));
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

    /**
     * Reenvía el correo de confirmación a la cuenta indicada.
     */
    public void reenviarCorreoDeConfirmacion() {
        Usuario usuario = jpaUsuario.buscarUsuario(cuenta);

        if (usuario == null || usuario.getConfirmada()) {
            return;
        }

        try {
            CorreoDeActivacion correo = new CorreoDeActivacion(usuario);
            correo.enviar();

            FacesMessage facesMessage
                    = new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Se ha reenviado el correo de confirmación a tu dirección de correo",
                            null);
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.addMessage("reenviarCorreo", facesMessage);
        } catch (MessagingException | IOException e) {
            FacesMessage facesMessage
                    = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "No se ha podido enviar el correo de confirmación. Vuelve a intentarlo más tarde.",
                            null);
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.addMessage("reenviarCorreo", facesMessage);
        }

    }

}
