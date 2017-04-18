package gucumatz.scpfc.controlador;

import gucumatz.scpfc.modelo.db.*;
import gucumatz.scpfc.modelo.Puesto;
import gucumatz.scpfc.modelo.FotospuestoPK;
import java.util.Locale;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.LinkedList;
import java.util.List;

/**
 * Clase para visualizar objetos de la clase Puesto
 *
 * @author Pablo Gerardo Gonzalez Lopez
 * @version 1.0
 */
@ManagedBean
//@RequestScoped
@ViewScoped
public class VisorPuesto {

    private final PuestoJpaController jpaPuesto;
    private final FotospuestoJpaController jpaFotospuesto;
    private Puesto puesto;
    private List<FotospuestoPK> fotospuesto;


    public VisorPuesto() {
        FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale("es-Mx"));
        this.jpaPuesto = new FabricaControladorJpa().obtenerControladorJpaPuesto();
        this.jpaFotospuesto = new FabricaControladorJpa().obtenerControladorJpaFotospuesto();
    }

    public void obtenerPuesto(Long id){
        this.puesto = jpaPuesto.findPuesto(id);
        this.fotospuesto = jpaFotospuesto.findFotospuestoById(id);
    }
    
    public Puesto getPuesto() {
        return this.puesto;
    }
}