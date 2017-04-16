package gucumatz.scpfc.controlador;

import gucumatz.scpfc.modelo.db.*;
import gucumatz.scpfc.modelo.Puesto;
import java.util.Locale;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 * Clase para controlar un objeto de la Clase Puesto
 *
 * @author Rivera Lopez Jorge Erick
 * @version 0.1
 */
@ManagedBean
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
        try {
            if (validaNombre(this.puesto.getNombre())) {
                if (this.puesto.getReferencias().length() == 0) {
                    this.puesto.setReferencias("Ninguna Referencia");
                }
                jpaPuesto.create(this.puesto);
                FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Puesto Agregado con exito", null);
                FacesContext.getCurrentInstance().addMessage(null, facesMessage);
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                redirecciona();
            }
        } catch (Exception e) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR GRAVE\n" + e.getMessage(), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }

    }

    public boolean validaNombre(String nombrePuesto) {
        Puesto p = jpaPuesto.findByNombre(nombrePuesto);
        if (p != null) {
            System.out.println("Longitud " + this.puesto.getLongitud() + " Latitud " + this.puesto.getLatitud());
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR\nEl nombre ya existe", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return false;
        }
        return true;
    }

    public void redirecciona() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("./Administrar.xhtml");
        } catch (Exception e) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
    }
}
