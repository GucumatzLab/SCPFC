package gucumatz.scpfc.web;

import gucumatz.scpfc.modelo.Usuario;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
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

    public CorreoDeActivacion(Usuario usuario) {
        if (usuario == null
                || usuario.getConfirmada()
                || usuario.getCodigoDeActivacion() == null) {
            throw new IllegalArgumentException();
        }

        this.usuario = usuario;
    }

    /**
     * Envía el correo de activación al usuario.
     */
    public void enviar()
            throws MessagingException {
        Session sesionEmail = obtenerSesionDeCorreo();

        MimeMessage mensaje = new MimeMessage(sesionEmail);

        mensaje.setFrom(new InternetAddress("scpfc@scpfc.com"));
        mensaje.addRecipients(Message.RecipientType.TO,
                String.format("%s <%s>",
                        usuario.getNombre(),
                        usuario.getCorreoElectronico()));
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
     * Crea la sesión de correo usada para el correo de activación.
     */
    private Session obtenerSesionDeCorreo() {
        Properties propiedadesSesionEmail = new Properties();
        propiedadesSesionEmail.setProperty("mail.smtp.port", "2000");
        propiedadesSesionEmail.setProperty("mail.smtp.host", "localhost");

        return Session.getInstance(propiedadesSesionEmail);
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
