package gucumatz.scpfc.web;

import gucumatz.scpfc.modelo.Usuario;
import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 * Un correo que se envía a un usuario nuevo para confirmar su cuenta.
 */
public class CorreoDeActivacion {

    /**
     * El usuario al que se enviará el correo. La cuenta del usuario no debe
     * haber sido confirmada y debe tener un código de verificación (no nulo).
     */
    private Usuario usuario;

    /**
     * La sesión de correo usada para el correo de activación.
     */
    private Session sesionEmail;

    /**
     * La dirección que se usa para enviar el correo.
     */
    private String nombre;
    private String direccion;

    public CorreoDeActivacion(Usuario usuario) throws IOException {
        if (usuario == null
                || usuario.getConfirmada()
                || usuario.getCodigoDeActivacion() == null) {
            throw new IllegalArgumentException();
        }

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();

        /* La cuenta que se usa para autenticarse en el servidor de correo. */
        final String usuarioCorreo;
        final String contrasena;

        try (InputStream is = externalContext.getResourceAsStream("WEB-INF/autenticacion-correo.properties")) {
            Properties prop = new Properties();
            prop.load(is);

            usuarioCorreo = prop.getProperty("usuario");
            contrasena = prop.getProperty("contrasena");
            nombre = prop.getProperty("nombre");
            direccion = prop.getProperty("correo");
        }

        this.usuario = usuario;

        try (InputStream is = externalContext.getResourceAsStream("WEB-INF/correo.properties")) {
            Properties prop = new Properties();
            prop.load(is);
            sesionEmail = Session.getInstance(prop,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(usuarioCorreo, contrasena);
                    }
                });
        }
    }

    /**
     * Envía el correo de activación al usuario.
     */
    public void enviar()
            throws MessagingException {
        MimeMessage mensaje = new MimeMessage(sesionEmail);

        String remitente = String.format("%s <%s>", nombre, direccion);
        String recipiente = String.format("%s <%s>",
                usuario.getNombre(),
                usuario.getCorreoElectronico());

        mensaje.setFrom(remitente);
        mensaje.addRecipients(Message.RecipientType.TO, recipiente);
        mensaje.setSubject("SCPFC - Confirma tu cuenta");

        String urlBase = obtenerDireccionBase();

        String textoMensaje = String.format(
                "Para confirmar tu cuenta ve a %s/activar-cuenta.xhtml?id=%d&codigo=%s",
                urlBase,
                usuario.getId(),
                usuario.getCodigoDeActivacion());
        mensaje.setText(textoMensaje);

        Transport.send(mensaje);
    }

    /**
     * Calcula la dirección base de la aplicación. Se usa para poder enviar una
     * URL completa.
     */
    private String obtenerDireccionBase() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletRequest solicitud = (HttpServletRequest) externalContext.getRequest();

        String urlSolicitud = solicitud.getRequestURL().toString();

        /* Obtiene el dominio donde está la aplicación. Por ejemplo,
         * http://localhost:8080/. Esto no incluye el contexto, por
         * ejemplo scpfc/ */
        int longitudDominio = urlSolicitud.length() - solicitud.getRequestURI().length();
        String dominio = urlSolicitud.substring(0, longitudDominio);

        /* Obtiene la dirección base de la aplicación pegándole el
         * contexto al dominio. */
        String baseAplicacion = dominio + solicitud.getContextPath();

        return baseAplicacion;
    }
}
