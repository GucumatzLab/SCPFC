package gucumatz.scpfc.controlador;

import gucumatz.scpfc.modelo.db.*;
import gucumatz.scpfc.modelo.*;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.faces.context.FacesContext;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;

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
    private FotoPuestoJpaController jpaFoto;
    private List<Puesto> puestos;
    private List<SelectItem> puestos2;
    private String seleccionado;

    /**
     * Metodo para inicializar los componentes de la clase.
     */
    @PostConstruct
    public void init() {
        FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale("es-Mx"));
        jpaPuesto = new FabricaControladorJpa().obtenerControladorJpaPuesto();
        jpaComentario = new FabricaControladorJpa().obtenerControladorJpaComentario();
        jpaCalificacion = new FabricaControladorJpa().obtenerControladorJpaCalificacion();
        jpaFoto = new FabricaControladorJpa().obtenerControladorJpaFotoPuesto();
        puestos = new LinkedList<Puesto>(jpaPuesto.findPuestoEntities());
        seleccionado = "";
        puestos2 = new LinkedList<SelectItem>();
        SelectItemGroup g = new SelectItemGroup();
        SelectItem[] si = new SelectItem[puestos.size()];
        int i = 0;
        for (Puesto p : puestos) {
            si[i] = new SelectItem(p.getNombre(), p.getNombre());
            i++;
        }
        g.setSelectItems(si);
        puestos2.add(g);
    }

    /**
     * Metodo para obtener el nombre del puesto seleccionado.
     *
     * @return Nombre del puesto seleccionado.
     */
    public String getSeleccionado() {
        return this.seleccionado;
    }

    /**
     * Metodo para cambiar el nombre del puesto seleccionado.
     *
     * @param nuevo - nuevo nombre.
     */
    public void setSeleccionado(String nuevo) {
        this.seleccionado = nuevo;
    }

    /**
     * Metodo para obtener la lista de SelectItem.
     *
     * @return Devuelve la Lista de SelectItem.
     */
    public List<SelectItem> getPuestos2() {
        return this.puestos2;
    }

    /**
     * Metodo para reemplazar la lista de SelectItem.
     *
     * @param l - Lista nueva.
     */
    public void setPuestos2(List<SelectItem> l) {
        this.puestos2 = l;
    }

    /**
     * Metodo con el que se hace la peticion al controlador para eliminar un
     * Puesto a la base de datos
     */
    public void elimina() {

        try {
            //if (jpaPuesto.findPuesto(Long.parseLong(this.id)) == null) {
            if (this.seleccionado.equals("")) {
                return;
            }
            Puesto p = jpaPuesto.findByNombre(this.seleccionado);
            if (p == null) {
                FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Advertencia:\nEl id no esta en la base de datos", null);
                FacesContext.getCurrentInstance().addMessage(null, facesMessage);
                return;
            }
            LinkedList<Comentario> com = new LinkedList<Comentario>(jpaComentario.findComentarioEntities());
            LinkedList<Calificacion> cal = new LinkedList<Calificacion>(jpaCalificacion.findCalificacionEntities());
            LinkedList<FotoPuesto> ft = new LinkedList<FotoPuesto>(jpaFoto.findFotoPuestoEntities());
            //Puesto p = jpaPuesto.findPuesto(Long.parseLong(this.id));

            for (Comentario c : com) {
                if (c.getPuesto() == p) {
                    jpaComentario.destroy(c.getId());
                }

            }
            for (Calificacion c : cal) {
                if (c.getPuesto() == p) {
                    jpaCalificacion.destroy(c.getId());
                }
            }

            for (FotoPuesto f : ft) {
                if (f.getPuesto().equals(p)) {
                    jpaFoto.destroy(f.getId());
                }
            }
            jpaPuesto.destroy(p.getId());

            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Puesto Eliminado con exito", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            redirecciona();
        } catch (Exception e) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR GRAVE", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
    }

    /**
     * Metodo para redireccionar la pagina actual a Administrar.xhtml.
     */
    public void redirecciona() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("./Administrar.xhtml");
        } catch (Exception e) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
    }
}
