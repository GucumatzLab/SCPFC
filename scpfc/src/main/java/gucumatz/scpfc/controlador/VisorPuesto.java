package gucumatz.scpfc.controlador;

import gucumatz.scpfc.modelo.db.*;
import gucumatz.scpfc.modelo.Puesto;
import gucumatz.scpfc.modelo.Calificacion;
import gucumatz.scpfc.modelo.Comentario;
import gucumatz.scpfc.modelo.FotoPuesto;
import java.io.Serializable;
import java.util.Locale;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.List;

/**
 * Clase controlador de DetallesPuesto.
 *
 * @author Pablo Gerardo Gonzalez Lopez
 * @version 1.0
 */
@ManagedBean
//@RequestScoped
@ViewScoped
public class VisorPuesto implements Serializable{

    private final PuestoJpaController jpaPuesto;
    private final FotoPuestoJpaController jpaFotospuesto;
    private final CalificacionJpaController jpaCalificacion;
    private final ComentarioJpaController jpaComentario;
    /* ID del puesto actual. */
    private Long id;
    /* Objeto del puesto actual. */
    private Puesto puesto;
    /* Lista de fotos del puesto actual. */
    private List<FotoPuesto> fotospuesto;
    /* Lista de calificaciones del puesto actual. */
    private List<Calificacion> calificacion;
    /* Lista de comentarios del puesto actual. */
    private List<Comentario> comentario;

    /**
     *<code>VisorPuesto</code> Constructor.
     */
    public VisorPuesto() {
        FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale("es-Mx"));
        this.jpaPuesto = new FabricaControladorJpa().obtenerControladorJpaPuesto();
        this.jpaFotospuesto = new FabricaControladorJpa().obtenerControladorJpaFotospuesto();
        this.jpaCalificacion = new FabricaControladorJpa().obtenerControladorJpaCalificacion();
        this.jpaComentario = new FabricaControladorJpa().obtenerControladorJpaComentario();
    }

    /**
     *<code>setId</code> Método actualiza el valor del ID del puesto actual.
     *@param l tipo <code>long</code>: ID del puesto.
     */
    public void setId(Long l){
        this.id = l;
    }

    /**
     *<code>getId</code> Método que regresa el ID del puesto actual.
     *@return tipo <code>long</code>: ID del puesto actual.
     */
    public Long getId(){
        return this.id;
    }

    /**
     *<code>obtenerPuesto</code> Método inicializa los objetos relacionados con el puesto actual.
     *@return tipo <code>String</code>: Dirección de redireccionamiento.
     */
    public String obtenerPuesto(){
        if (this.id == null) {
            return "index";
        }

        this.puesto = jpaPuesto.findPuesto(this.id);
        if (this.puesto == null) {
                return "index";
        }
        this.fotospuesto = jpaFotospuesto.findFotospuestoByPuestoId(puesto);
        this.calificacion = jpaCalificacion.findAllByPuestoID(this.puesto);
        this.comentario = jpaComentario.findAllByPuestoID(this.puesto);

        return null;
    }

    /**
     *<code>getPuesto</code> Método que regresa el puesto actual.
     *@return tipo <code>Puesto</code>: Objeto del puesto actual.
     */
    public Puesto getPuesto() {
        return this.puesto;
    }

    /**
     *<code>getFotosPuesto</code> Método que regresa la lista de fotos del puesto actual.
     *@return tipo <code>List<Fotospuesto></code>: Lista de fotos del puesto actual.
     */
    public List<FotoPuesto> getFotospuesto(){
        return this.fotospuesto;
    }

    /**
     *<code>getPromedioCalificacion</code> Método que regresa el promedio de calificaciones del puesto actual.
     *@return tipo <code>int</code>: Promedio de calificaciones del puesto actual. (Valores entre 0 - 5)
     */
    public int getPromedioCalificacion(){
        float promedio = 0;
        if(this.calificacion != null) {
            for(Calificacion c : this.calificacion) {
                promedio = promedio + c.getCalificacion();
            }
            promedio = promedio / this.calificacion.size();
        }
        return Math.round(promedio);
    }
    
    /**
     *<code>getComentario</code> Método que regresa la lista de comentarios del puesto actual.
     *@return tipo <code>List<Comentario></code>: Lista de comentarios del puesto actual.
     */
    public List<Comentario> getComentario(){
        return this.comentario;
    }

    /**
     *<code>getCalificacion</code> Método que regresa la lista de calificaciones del puesto actual.
     *@return tipo <code>List<Calificacion></code>: Lista de calificaciones del puesto actual.
     */
    public List<Calificacion> getCalificaciones(){
        return this.calificacion;
    }
}
