package gucumatz.scpfc.controlador;

import gucumatz.scpfc.modelo.db.*;
import gucumatz.scpfc.modelo.Puesto;
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

public class EliminadorPuesto implements Serializable{

    private PuestoJpaController jpaPuesto;
    private List<Puesto> puestos;
    private String id = "";

    /*
    @PostConstruct
    public void init() {

    }*/
    public EliminadorPuesto() {
        FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale("es-Mx"));
        jpaPuesto = new FabricaControladorJpa().obtenerControladorJpaPuesto();
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

            jpaPuesto.destroy(Long.parseLong(this.id));
            redirecciona();
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Puesto Eliminado con exito", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (NumberFormatException e) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error debe ser un numer", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (Exception e) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error : " + e.getMessage(), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
    }

    public void redirecciona() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("./Administrar2.xhtml");
        } catch (Exception e) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
    }
}
