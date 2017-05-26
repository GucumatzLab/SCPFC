package gucumatz.scpfc.controlador;

import gucumatz.scpfc.modelo.Comentario;
import gucumatz.scpfc.modelo.Reaccion;
import gucumatz.scpfc.modelo.Usuario;
import gucumatz.scpfc.modelo.db.ControladorJpaComentario;
import gucumatz.scpfc.modelo.db.ControladorJpaReaccion;
import gucumatz.scpfc.modelo.db.ControladorJpaUsuario;
import gucumatz.scpfc.modelo.db.FabricaControladorJpa;
import gucumatz.scpfc.modelo.db.exceptions.NonexistentEntityException;

import java.io.IOException;
import java.io.Serializable;
import javax.faces.application.FacesMessage;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

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

    /**
     * Cierra la sesión actual.
     *
     * @return la página a la que se debe regresar
     */
    public String cerrarSesion() {
        usuario = null;
        return obtenerPaginaAnterior();
    }

    /**
     * Obtiene al usuario actual.
     *
     * @return el usuario actual o null si no ha iniciado sesión
     */
    public Usuario obtenerUsuarioActual() {
        return usuario;
    }

    /**
     * Regresa la foto del usuario actual, o una imagen por omisión si no tiene
     * o no ha iniciado sesión.
     *
     * @return la foto del usuario actual con un tipo adecuado para
     * {@code p:graphicImage}
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
     *
     * @return true si hay una sesión activa, false en otro caso
     */
    public boolean getHaySesionActiva() {
        return usuario != null;
    }

    /**
     * Dice si el usuario actual es un administrador.
     *
     * @return true si hay una sesión de administrador activa, false si no hay
     * una sesión activa o el usuario actual no es administrador
     */
    public boolean getEsAdministrador() {
        return usuario != null && usuario.getEsAdministrador();
    }

    /**
     * Redirige a la página principal.
     *
     * @return una cadena que redirige a la página principal
     */
    public String redirigeAPaginaPrincipal() {
        return "index?faces-redirect=true";
    }

    /**
     * Obtiene la dirección de la página anterior y olvida cuál era
     * (establece el atributo en null). En caso que no haya ninguna
     * registrada, regresa "index".
     *
     * @return una cadena que redirige a la página anterior, o a la página
     * principal si no se registró una página anterior
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
     *
     * @return una cadena que redirige a la página para iniciar sesión
     */
    public String irAIniciarSesion() {
        return "iniciar-sesion?faces-redirect=true";
    }

    /**
     * <code>reacciona</code> Método que dado un comentario y una reaccion:
     * Si ya hay una reaccion a ese comentario y es la misma que la que llamó
     * a este método, se elimina, si es diferente se actualiza.
     * Si no hay una reaccion se crea una nueva,
     *
     * @param id_comentario tipo <code>long</code>: id del comentario
     * @param reaccion tipo <code>int</code>: reaccion
     */
    public void reacciona(long id_comentario, int reaccion) throws IOException {
        Reaccion r = null;
        
        for(Reaccion rr : usuario.getReacciones()){
            if (rr.getComentarioId().getId() == id_comentario) {
                r = rr;
                break;
            }
        }

        FabricaControladorJpa fab = new FabricaControladorJpa();
        ControladorJpaReaccion jpaReaccion
            = fab.obtenerControladorJpaReaccion();
        if (r == null) {
            r = new Reaccion();
            
            ControladorJpaComentario jpaComentario
                = fab.obtenerControladorJpaComentario();
            Comentario c = jpaComentario.findComentario(id_comentario);
            r.setComentarioId(c);
            c.getReacciones().add(r);
            
            r.setUsuarioId(usuario);
            usuario.getReacciones().add(r);
            
            r.setReaccion(reaccion);

            jpaReaccion.crear(r);

        } else {
            if (r.getReaccion() == reaccion) {
                try {
                    usuario.getReacciones().remove(r);

                    ControladorJpaComentario jpaComentario
                        = fab.obtenerControladorJpaComentario();
                    Comentario c = jpaComentario.findComentario(id_comentario);
                    c.getReacciones().remove(r);
                    
                    jpaReaccion.destruir(r.getId());

                } catch (NonexistentEntityException e) {
                }
            } else {
                r.setReaccion(reaccion);
                
                try {
                    jpaReaccion.editar(r);

                } catch (Exception e) {
                }
            }
        }

        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect("DetallesPuesto.xhtml?id=" + r.getComentarioId().getPuesto().getId());
    }

    /**
     * <code>reaccion</code> Método que checa si un usuario tiene una reaccion
     * con los parametros dados.
     *
     * @param id_comentario tipo <code>long</code>: id del comentario
     * @param reaccion tipo <code>int</code>: reaccion
     */
    public boolean reaccion(long id_comentario, int reaccion) {
        Reaccion r = null;
        
        for(Reaccion rr : usuario.getReacciones()){
            if (rr.getComentarioId().getId() == id_comentario) {
                r = rr;
                break;
            }
        }
        
        if (r != null) {
            return r.getReaccion() == reaccion;
        }
        
        return false;
    }
}
