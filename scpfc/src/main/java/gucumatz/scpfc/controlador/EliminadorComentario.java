package gucumatz.scpfc.controlador;

import gucumatz.scpfc.modelo.Comentario;
import gucumatz.scpfc.modelo.Puesto;
import gucumatz.scpfc.modelo.Usuario;
import gucumatz.scpfc.modelo.db.ControladorJpaComentario;
import gucumatz.scpfc.modelo.db.FabricaControladorJpa;
import gucumatz.scpfc.modelo.db.exceptions.NonexistentEntityException;

import java.util.List;
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

    /**
    * Puesto al que pertenece el comentario
    */
    private List<Comentario> comentarios;

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

    public void setComentarios(List<Comentario> nuevo) {
        this.comentarios = nuevo;
    }
    /*
    public void setPuesto(Puesto nuevo){
        this.puesto = nuevo;
    }*/

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
        Comentario comentario = jpaComentario.findComentario(idComentario);
        if (sesionActiva.getEsAdministrador()) {
            /* Un administrador puede eliminar cualquier comentario. */
            tienePermisos = true;
        } else {
            /* Si no es administrador, debe ser quien hizo el comentario. */
            Usuario usuarioActual = sesionActiva.getUsuario();

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
            comentarios.remove(comentario);
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
