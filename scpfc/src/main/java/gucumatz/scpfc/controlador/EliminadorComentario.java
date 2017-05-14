package gucumatz.scpfc.controlador;

import gucumatz.scpfc.modelo.Comentario;
import gucumatz.scpfc.modelo.db.ControladorJpaComentario;
import gucumatz.scpfc.modelo.db.FabricaControladorJpa;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

/**
 * @author lchacon
 */
@ManagedBean(name="eliminadorComentario")
@RequestScoped
public class EliminadorComentario {

    private ControladorJpaComentario jpaComentario;

    public EliminadorComentario() {
        jpaComentario = new FabricaControladorJpa().obtenerControladorJpaComentario();
    }

    public void eliminarComentario() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            Long idComentario = Long.parseLong(facesContext.getExternalContext().getRequestParameterMap().get("idComentario"));
            jpaComentario.destruir(idComentario);
        } catch (Exception e) {

        }
    }

}
