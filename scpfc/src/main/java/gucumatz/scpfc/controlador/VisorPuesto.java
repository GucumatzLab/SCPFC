package gucumatz.scpfc.controlador;

import gucumatz.scpfc.modelo.db.*;
import gucumatz.scpfc.modelo.Puesto;
import gucumatz.scpfc.modelo.Calificacion;
import gucumatz.scpfc.modelo.Comentario;
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
    private final CalificacionJpaController jpaCalificacion;
    private final ComentarioJpaController jpaComentario;
    private Puesto puesto;
    private List<FotospuestoPK> fotospuesto;
    private List<Calificacion> calificacion;
    private List<Comentario> comentario;

    public VisorPuesto() {
        FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale("es-Mx"));
        this.jpaPuesto = new FabricaControladorJpa().obtenerControladorJpaPuesto();
        this.jpaFotospuesto = new FabricaControladorJpa().obtenerControladorJpaFotospuesto();
        this.jpaCalificacion = new FabricaControladorJpa().obtenerControladorJpaCalificacion();
        this.jpaComentario = new FabricaControladorJpa().obtenerControladorJpaComentario();
    }

    public void obtenerPuesto(Long id){
        this.puesto = jpaPuesto.findPuesto(id);
        this.fotospuesto = jpaFotospuesto.findFotospuestoById(id);
        this.calificacion = jpaCalificacion.findAllByPuestoID(this.puesto);
        this.comentario = jpaComentario.findAllByPuestoID(this.puesto);
    }
    
    public Puesto getPuesto() {
        return this.puesto;
    }
    
    public List<FotospuestoPK> getFotospuesto(){
        return this.fotospuesto;
    }
    
    public int getPromedioCalificacion(){
        float promedio = 0;
        for(Calificacion c : this.calificacion) {
            promedio = promedio + c.getCalificacion();
        }
        promedio = promedio / this.calificacion.size();
        return Math.round(promedio);
    }
    
    public List<Comentario> getComentario(){
        return this.comentario;
    }
}