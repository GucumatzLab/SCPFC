package gucumatz.scpfc.controlador;

import gucumatz.scpfc.modelo.Comentario;
import gucumatz.scpfc.modelo.Usuario;
import gucumatz.scpfc.modelo.db.ControladorJpaComentario;
import gucumatz.scpfc.modelo.db.FabricaControladorJpa;
import gucumatz.scpfc.modelo.db.exceptions.NonexistentEntityException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

/**
 * @author lchacon
 */
@ManagedBean(name = "eliminadorComentario")
@RequestScoped
public class EliminadorComentario {

    private ControladorJpaComentario jpaComentario;

    /**
     * El ID del comentario que se quiere eliminar.
     */
    private Long idComentario;

    @ManagedProperty("#{sesionActiva}")
    private SesionActiva sesionActiva;

    public EliminadorComentario() {
        jpaComentario
            = new FabricaControladorJpa().obtenerControladorJpaComentario();
    }

    public void setIdComentario(Long idComentario) {
        this.idComentario = idComentario;
    }

    public void setSesionActiva(SesionActiva sesionActiva) {
        this.sesionActiva = sesionActiva;
    }

    /**
     * Elimina un comentario. El comentario es identificado mediante un
     * parámetro en la solicitud llamado "idComentario".
     */
    public void eliminarComentario() {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        /*
         * Vemos si el usuario actual tiene permisos para eliminar el
         * comentario.
         */
        boolean tienePermisos = false;
        if (sesionActiva.getEsAdministrador()) {
            /* Un administrador puede eliminar cualquier comentario. */
            tienePermisos = true;
        } else {
            /* Si no es administrador, debe ser quien hizo el comentario. */
            Usuario usuarioActual = sesionActiva.getUsuario();
            Comentario comentario = jpaComentario.findComentario(idComentario);

            if (usuarioActual != null && comentario != null
                    && usuarioActual.equals(comentario.getUsuario())) {
                tienePermisos = true;
            }
        }

        /* Si no tiene permisos, no hacemos nada. */
        if (!tienePermisos) {
            return;
        }

        try {
            jpaComentario.destruir(idComentario);

            FacesMessage facesMessage
                = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Comentario eliminado.", null);
            facesContext.addMessage(null, facesMessage);
        } catch (NonexistentEntityException e) {
            /* El comentario no existe. */
        }
    }

}
