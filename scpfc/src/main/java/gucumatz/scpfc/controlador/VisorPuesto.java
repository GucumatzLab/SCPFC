package gucumatz.scpfc.controlador;

import gucumatz.scpfc.modelo.*;
import gucumatz.scpfc.modelo.Puesto;
import gucumatz.scpfc.modelo.db.*;

import java.io.InputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Properties;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

/** Clase controlador de DetallesPuesto.
 *
 * @author Pablo Gerardo Gonzalez Lopez
 * @version 1.0
 */
@ManagedBean
//@RequestScoped
@ViewScoped
public class VisorPuesto implements Serializable {

    private final ControladorJpaPuesto jpaPuesto;
    private final ControladorJpaCalificacion jpaCalificacion;
    /* ID del puesto actual. */
    private Long id;
    /* Objeto del puesto actual. */
    private Puesto puesto;

    /**
     *<code>VisorPuesto</code> Constructor.
     */
    public VisorPuesto() {
        FabricaControladorJpa fabricaJpa = new FabricaControladorJpa();
        this.jpaPuesto = fabricaJpa.obtenerControladorJpaPuesto();
        this.jpaCalificacion = fabricaJpa.obtenerControladorJpaCalificacion();
    }

    /**
     *<code>setId</code> Método actualiza el valor del ID del puesto actual.
     *@param l tipo <code>long</code>: ID del puesto.
     */
    public void setId(Long l) {
        this.id = l;
    }

    /**
     *<code>getId</code> Método que regresa el ID del puesto actual.
     *@return tipo <code>long</code>: ID del puesto actual.
     */
    public Long getId() {
        return this.id;
    }

    /**
     * <code>obtenerPuesto</code> Método inicializa los objetos relacionados con
     * el puesto actual.
     *
     *@return tipo <code>String</code>: Dirección de redireccionamiento.
     */
    public String obtenerPuesto() {
        if (this.id == null) {
            return "index";
        }

        this.puesto = jpaPuesto.buscarPorId(this.id);
        if (this.puesto == null) {
            return "index";
        }

        return null;
    }

    /**
     *<code>getPuesto</code> Método que regresa el puesto actual.
     *@return tipo <code>Puesto</code>: Objeto del puesto actual.
     */
    public Puesto getPuesto() {
        return this.puesto;
    }

    /**
     * <code>getPromedioCalificacion</code> Método que regresa el promedio de
     * calificaciones del puesto actual.
     *
     * @return tipo <code>int</code>: Promedio de calificaciones del puesto
     * actual. (Valores entre 0 - 5)
     */
    public int getPromedioCalificacion() {
        return (int) jpaCalificacion.promedioDePuesto(puesto);
    }


    /**
    * Metodo que envia mensaje al Administrador
    * @param usuario - usuario que emitio la alerta
    * @param acusado - usuario acusado
    * @param c - comentario del usuario acusado
    * @param pst - puesto
    */
    public void enviaMensaje(Usuario usuario, Usuario acusado,
                                         Comentario c, Puesto pst) {
        /* La cuenta que se usa para autenticarse en el servidor de correo. */
        try {
            LinkedList<Usuario> us = new LinkedList<Usuario>(
                new FabricaControladorJpa().obtenerControladorJpaUsuario()
                .findUsuarioEntities());
            Usuario admin = null;
            for (Usuario u : us) {
                if (u.getEsAdministrador()) {
                    admin = u;
                    break;
                }
            }
            final String usuarioCorreo;
            final String contrasena;
            String direccion;
            Session sesionEmail;
            String nombre;
            String archivoPropiedadesAutenticacion =
                   "WEB-INF/autenticacion-correo.properties";
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            try (InputStream is = externalContext
                .getResourceAsStream(archivoPropiedadesAutenticacion)) {
                Properties prop = new Properties();
                prop.load(is);

                usuarioCorreo = prop.getProperty("usuario");
                contrasena = prop.getProperty("contrasena");
                nombre = prop.getProperty("nombre");
                direccion = prop.getProperty("correo");
            }

            String archivoPropiedadesCorreo = "WEB-INF/correo.properties";
            try (InputStream is = externalContext
                .getResourceAsStream(archivoPropiedadesCorreo)) {
                Properties prop = new Properties();
                prop.load(is);
                Authenticator autenticador =
                    new Authenticator() {
                    @Override
                    protected PasswordAuthentication
                        getPasswordAuthentication() {
                        PasswordAuthentication pa
                            = new PasswordAuthentication(usuarioCorreo,
                                contrasena);
                        return pa;
                    }
                };
                sesionEmail = Session.getInstance(prop, autenticador);
            }

            MimeMessage mensaje = new MimeMessage(sesionEmail);

            String remitente = String.format("%s <%s>",
                "Administracion-auto", direccion);
            String recipiente = String.format("%s <%s>",
                "Administracion-auto",
                admin.getCorreoElectronico() + "ciencias.unam.mx");
            mensaje.setFrom(remitente);
            mensaje.addRecipients(Message.RecipientType.TO, recipiente);
            mensaje.setSubject("SCPFC - Comentario inapropiado u ofensivo");

            String textoMensaje = "El mensaje con id <"
                + c.getId()
                + "> de fecha <"
                + c.getFecha()
                + "> del usuario <"
                + acusado.getNombre()
                + "> ha sido considerado ofensivo por el usuario <"
                + usuario.getNombre()
                + ">\n\nREVISA EL COMENTARIO\n";
            String uri = ((HttpServletRequest) externalContext.getRequest())
                   .getRequestURI();
            mensaje.setText(textoMensaje
                                + "http://localhost:8080"
                                + uri
                                + "?id="
                                + pst.getId());
            Transport.send(mensaje);
            FacesMessage facesMessage
                = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "REPORTE ENVIADO", "");
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (MessagingException e) {
            FacesMessage facesMessage
                = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "ERROR GRAVE", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (Exception k) {
            FacesMessage facesMessage
                = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "ERROR GRAVE", k.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
    }

}
