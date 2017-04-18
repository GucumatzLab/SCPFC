package gucumatz.scpfc.controlador;

import gucumatz.scpfc.modelo.db.*;
import gucumatz.scpfc.modelo.*;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Clase para controlar un objeto de la Clase Puesto
 *
 * @author Rivera Lopez Jorge Erick
 * @version 0.1
 */
@ManagedBean
@ViewScoped

public class EliminadorPuesto implements Serializable {

    private PuestoJpaController jpaPuesto;
    private ComentarioJpaController jpaComentario;
    private CalificacionJpaController jpaCalificacion;
    private FotospuestoJpaController jpaFoto;

    private List<Puesto> puestos;
    private String id = "";

    public EliminadorPuesto() {
        FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale("es-Mx"));
        jpaPuesto = new FabricaControladorJpa().obtenerControladorJpaPuesto();
        jpaComentario = new FabricaControladorJpa().obtenerControladorJpaComentario();
        jpaCalificacion = new FabricaControladorJpa().obtenerControladorJpaCalificacion();
        jpaFoto = new FabricaControladorJpa().obtenerControladorJpaFotospuesto();
        puestos = new LinkedList<Puesto>(jpaPuesto.findPuestoEntities());
    }

    public List<Puesto> getPuestos() {
        return this.puestos;
    }

    public void setPuestos(List<Puesto> l) {
        this.puestos = l;
    }

    public String getId() {
        return this.id;
    }

    /**
     * Metodo para cambiar el valor de Puesto
     *
     * @param nuevo con el que se reemplazará
     */
    public void setId(String nuevo) {
        this.id = nuevo;
    }

    /**
     * Metodo con el que se hace la peticion al controlador para agregar un
     * Puesto a la base de datos
     */
    public void elimina() {

        try {
            if (jpaPuesto.findPuesto(Long.parseLong(this.id)) == null) {
                FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Advertencia:\nEl id no esta en la base de datos",null);
                FacesContext.getCurrentInstance().addMessage(null, facesMessage);
                return;
            }
            LinkedList<Comentario> com = new LinkedList<Comentario>(jpaComentario.findComentarioEntities());
            LinkedList<Calificacion> cal = new LinkedList<Calificacion>(jpaCalificacion.findCalificacionEntities());
            LinkedList<Fotospuesto> ft = new LinkedList<Fotospuesto>(jpaFoto.findFotospuestoEntities());
            Puesto p = jpaPuesto.findPuesto(Long.parseLong(this.id));
            for (Comentario c : com) {
                if (c.getPuestoId() == p) {
                    jpaComentario.destroy(c.getId());
                }

            }
            for (Calificacion c : cal) {
                if (c.getPuestoId() == p) {
                    jpaCalificacion.destroy(c.getId());
                }
            }

            for (Fotospuesto f : ft) {
                if (f.getPuesto().equals(p)) {
                    jpaFoto.destroy(f.getFotospuestoPK());
                }
            }
            jpaPuesto.destroy(Long.parseLong(this.id));

            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Puesto Eliminado con exito", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            redirecciona();
        } catch (NumberFormatException e) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR\nDebe ser un numero", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (Exception e) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR GRAVE\n"+e.getMessage(),null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
    }

    public void redirecciona() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("./Administrar.xhtml");
        } catch (Exception e) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
    }
}