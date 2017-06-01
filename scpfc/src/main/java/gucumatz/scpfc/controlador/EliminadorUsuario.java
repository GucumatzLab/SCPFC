package gucumatz.scpfc.controlador;

import gucumatz.scpfc.modelo.Calificacion;
import gucumatz.scpfc.modelo.Comentario;
import gucumatz.scpfc.modelo.Reaccion;
import gucumatz.scpfc.modelo.Usuario;
import gucumatz.scpfc.modelo.db.ControladorJpaCalificacion;
import gucumatz.scpfc.modelo.db.ControladorJpaComentario;
import gucumatz.scpfc.modelo.db.ControladorJpaReaccion;
import gucumatz.scpfc.modelo.db.ControladorJpaUsuario;
import gucumatz.scpfc.modelo.db.FabricaControladorJpa;
import gucumatz.scpfc.modelo.db.exceptions.IllegalOrphanException;
import gucumatz.scpfc.modelo.db.exceptions.NonexistentEntityException;

import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;


/**
 * @author Jorge
 */
@ManagedBean(name = "eliminadorUsuario")
@RequestScoped
public class EliminadorUsuario implements java.io.Serializable {

    private ControladorJpaCalificacion jpaCalificacion;
    private ControladorJpaComentario jpaComentario;
    private ControladorJpaReaccion jpaReaccion;
    private ControladorJpaUsuario jpaUsuario;

    /**
     * El ID del comentario que se quiere eliminar.
     */
    private Long usuarioId;
    private List<Usuario> usuarios;

    public EliminadorUsuario() {
        jpaComentario
            = new FabricaControladorJpa().obtenerControladorJpaComentario();
        jpaReaccion
            = new FabricaControladorJpa().obtenerControladorJpaReaccion();
        jpaUsuario
            = new FabricaControladorJpa().obtenerControladorJpaUsuario();
        jpaCalificacion
            = new FabricaControladorJpa().obtenerControladorJpaCalificacion();
        usuarios
            =  jpaUsuario.findUsuarioEntities();
    }

    public Long getUsuarioId() {
        return this.usuarioId;
    }

    public List<Usuario> getUsuarios() {
        return this.usuarios;
    }

    public void setUsuarioId(Long nuevo) {
        this.usuarioId = nuevo;
    }

    public void setUsuarios(List<Usuario> nuevo) {
        this.usuarios = nuevo;
    }

    /**
     * Elimina un Usuario.
     * @throws IllegalOrphanException --
     */
    public void eliminarUsuario() throws IllegalOrphanException {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        Usuario usuario = jpaUsuario.buscarPorId(usuarioId);

        try {

            for (Comentario com : usuario.getComentarios()) {
                for (Reaccion r : com.getReacciones()) {
                    jpaReaccion.destruir(r.getId());
                    usuario.getReacciones().remove(r);
                }
                jpaComentario.destruir(com.getId());
            }

            for (Reaccion r : usuario.getReacciones()) {
                jpaReaccion.destruir(r.getId());
            }

            for (Calificacion cal : usuario.getCalificaciones()) {
                jpaCalificacion.destruir(cal.getId());
            }

            if (usuario.getRutaImagen() != null) {
                ManejadorDeImagenes mdi = new ManejadorDeImagenes();
                mdi.eliminarImagen(usuario.getRutaImagen());
            }

            usuarios.remove(usuario);
            jpaUsuario.destruir(usuarioId);

            FacesMessage facesMessage
                = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Usuario eliminado.", null);
            facesContext.addMessage(null, facesMessage);
            facesContext.getExternalContext()
                    .getFlash().setKeepMessages(true);
            redirecciona();
        } catch (Exception e) {
            FacesMessage facesMessage
                = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Error al tratar de eliminar " + e, null);
            facesContext.addMessage(null, facesMessage);
        }
    }

    /**
     * Metodo que redirecciona la página Usuarios.xhtml.
     */
    public void redirecciona() {
        try {
            FacesContext.getCurrentInstance().getExternalContext()
                .redirect("./Usuarios.xhtml");
        } catch (Exception e) {
            FacesMessage facesMessage
                = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "ERROR 01" + e.toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
    }

}
