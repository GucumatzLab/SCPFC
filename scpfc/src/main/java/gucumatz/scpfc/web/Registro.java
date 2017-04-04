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
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import javax.faces.context.ExternalContext;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;

//
@ManagedBean
@ViewScoped
public class Registro implements Serializable {

    private final UsuarioJpaController jpaUsuario;

    private String nombreDeUsuario;
    private String correoElectronico;
    private String contrasena;
    private String confirmacionDeContrasena;

    private FacesContext facesContext;

    private boolean hayErrores;

    /**
     * Creates a new instance of Registro
     */
    public Registro() {
        jpaUsuario = new FabricaControladorJpa().obtenerControladorJpaUsuario();
        hayErrores = false;
    }

    public String getNombreDeUsuario() {
        return nombreDeUsuario;
    }

    public void setNombreDeUsuario(String nombreDeUsuario) {
        this.nombreDeUsuario = nombreDeUsuario;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getConfirmacionDeContrasena() {
        return confirmacionDeContrasena;
    }

    public void setConfirmacionDeContrasena(String confirmacionDeContrasena) {
        this.confirmacionDeContrasena = confirmacionDeContrasena;
    }

    public void validarDatos() {
        facesContext = FacesContext.getCurrentInstance();
        hayErrores = false;

        comprobarNombreDeUsuarioDisponible();
        comprobarCorreoElectronicoDisponible();
        comprobarCorreoElectronicoEsDeCiencias();
        comprobarContrasenasCoinciden();
    }

    public String registrar() {
        validarDatos();

        if (hayErrores) {
            return null;
        }

        Usuario u = new Usuario();
        u.setNombre(nombreDeUsuario);
        u.setCorreoElectronico(correoElectronico);
        u.setContrasena(contrasena);
        u.setConfirmada(false);
        u.setEsAdministrador(false);
        u.setCodigoDeActivacion("asdf");

        jpaUsuario.create(u);

        enviarCorreoDeActivacion(u);
        return "index.xhtml";
    }

    private void comprobarNombreDeUsuarioDisponible() {
        Usuario usuario = jpaUsuario.findByNombre(nombreDeUsuario);
        if (usuario != null) {
            agregarError("El nombre de usuario ya existe", "nombreDeUsuario");
        }
    }

    private void comprobarCorreoElectronicoDisponible() {
        Usuario usuario = jpaUsuario.findByCorreoElectronico(correoElectronico);
        if (usuario != null) {
            agregarError("El correo electrónico ya existe", "correoElectronico");
        }
    }

    private void comprobarCorreoElectronicoEsDeCiencias() {
        if (!correoElectronico.endsWith("@ciencias.unam.mx")) {
            agregarError("El correo electrónico debe ser de @ciencias.unam.mx", "correoElectronico");
        }
    }

    private void comprobarContrasenasCoinciden() {
        if (!contrasena.equals(confirmacionDeContrasena)) {
            agregarError("Las contraseñas no coinciden", "confirmacionDeContrasena");
        }
    }

    private void agregarError(String mensaje, String elemento) {
        hayErrores = true;
        FacesMessage facesMessage
                = new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, null);
        facesContext.addMessage(null, facesMessage);
    }

    public void enviarCorreoDeActivacion(Usuario usuario) {
        Properties propiedadesSesionEmail = new Properties();
        propiedadesSesionEmail.setProperty("mail.smtp.port", "2000");
        propiedadesSesionEmail.setProperty("mail.smtp.host", "localhost");
        Session sesionEmail = Session.getInstance(propiedadesSesionEmail);

        try {
            MimeMessage mensaje = new MimeMessage(sesionEmail);
            mensaje.setFrom(new InternetAddress("scpfc@scpfc.com"));
            mensaje.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(usuario.getCorreoElectronico(), usuario.getNombre()));
            mensaje.setSubject("SCPFC - Confirma tu cuenta");

            ExternalContext context = facesContext.getExternalContext();
            HttpServletRequest request = (HttpServletRequest) context.getRequest();
            String url = request.getRequestURL().toString();
            String baseUrl = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath() + "/";

            String textoMensaje
                    = "Para confirmar tu cuenta ve a "
                    + baseUrl
                    + "/activar-cuenta"
                    + "?id=" + usuario.getId()
                    + "&codigo=" + usuario.getCodigoDeActivacion();
            mensaje.setText(textoMensaje);

            Transport.send(mensaje);
        } catch (MessagingException | java.io.UnsupportedEncodingException me) {

        }
    }

}
