package gucumatz.scpfc.controlador;

import gucumatz.scpfc.modelo.db.*;
import gucumatz.scpfc.modelo.Puesto;
import java.util.Locale;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
//import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
//import gucumatz.scpfc.web.Mapa;

/**
 * Clase para controlar un objeto de la Clase Puesto
 *
 * @author Rivera Lopez Jorge Erick
 * @version 0.1
 */
@ManagedBean
//@RequestScoped
@ViewScoped
public class AgregadorPuesto {

    private final PuestoJpaController jpaPuesto;

    private Puesto puesto = new Puesto();


    public AgregadorPuesto() {
        FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale("es-Mx"));
        jpaPuesto = new FabricaControladorJpa().obtenerControladorJpaPuesto();
    }

    public Puesto getPuesto() {
        return puesto;
    }

    /**
     * Metodo para cambiar el valor de Puesto
     *
     * @param puesto con el que se reemplazará
     */
    public void setPuesto(Puesto puesto) {
        this.puesto = puesto;
    }

    /**
     * Metodo con el que se hace la peticion al controlador para agregar un
     * Puesto a la base de datos
     */
    public void agrega() {
        if (validaNombre(this.puesto.getNombre())) {
            this.puesto.setReferencias("");
            jpaPuesto.create(this.puesto);
            redirecciona();
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Puesto Agregado con exito", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }

    }

    public boolean validaNombre(String nombrePuesto) {
        Puesto p = jpaPuesto.findByNombre(nombrePuesto);
        if (p != null) {
            System.out.println("Longitud " + this.puesto.getLongitud() + " Latitud " + this.puesto.getLatitud());
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "El nombre ya existe o esta vacio", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return false;
        }
        return true;
    }

    public void redirecciona() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("./Administrar2.xhtml");
        } catch (Exception e) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
    }
}
