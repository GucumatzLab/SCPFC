package gucumatz.scpfc.controlador;

import gucumatz.scpfc.modelo.db.*;
import gucumatz.scpfc.modelo.Puesto;
import gucumatz.scpfc.modelo.FotosPuesto;
import java.util.Locale;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.LinkedList;

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
    private final FotosPuestoJpaController jpaFotosPuesto;
    private Puesto puesto;
    private FotosPuesto fotosPuesto;


    public VisorPuesto() {
        FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale("es-Mx"));
        jpaPuesto = new FabricaControladorJpa().obtenerControladorJpaPuesto();
        jpaFotosPuesto = new FabricaControladorJpa().obtenerControladorJpaFotosPuesto();
    }

    public void obtenerPuesto(Long id){
        this.puesto = jpaPuesto.findPuesto(id);
        this.fotosPuesto = new LinkedList<FotosPuesto>(jpaFotosPuesto.findPuestoEntities());
    }
    
    public Puesto getPuesto() {
        return this.puesto;
    }
}