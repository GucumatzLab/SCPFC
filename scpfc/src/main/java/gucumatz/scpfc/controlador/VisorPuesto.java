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

    private final ControladorJpaPuesto jpaPuesto;
    private final ControladorJpaCalificacion jpaCalificacion;
    /* ID del puesto actual. */
    private Long id;
    /* Objeto del puesto actual. */
    private Puesto puesto;

    /**
     *<code>VisorPuesto</code> Constructor.
     */
    public VisorPuesto() {
        FacesContext.getCurrentInstance().getViewRoot()
            .setLocale(new Locale("es-Mx"));
        FabricaControladorJpa fabricaJpa = new FabricaControladorJpa();
        this.jpaPuesto = fabricaJpa.obtenerControladorJpaPuesto();
        this.jpaCalificacion = fabricaJpa.obtenerControladorJpaCalificacion();
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
     * <code>obtenerPuesto</code> Método inicializa los objetos relacionados con
     * el puesto actual.
     *
     *@return tipo <code>String</code>: Dirección de redireccionamiento.
     */
    public String obtenerPuesto(){
        if (this.id == null) {
            return "index";
        }

        this.puesto = jpaPuesto.buscarPorId(this.id);
        if (this.puesto == null) {
                return "index";
        }

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
     * <code>getPromedioCalificacion</code> Método que regresa el promedio de
     * calificaciones del puesto actual.
     *
     * @return tipo <code>int</code>: Promedio de calificaciones del puesto
     * actual. (Valores entre 0 - 5)
     */
    public int getPromedioCalificacion(){
        return (int) jpaCalificacion.promedioDePuesto(puesto);
    }

}
