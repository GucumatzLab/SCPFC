package gucumatz.scpfc.controlador;

import gucumatz.scpfc.modelo.Usuario;
import gucumatz.scpfc.modelo.Calificacion;
import gucumatz.scpfc.modelo.Comentario;
import gucumatz.scpfc.modelo.Reaccion;
import gucumatz.scpfc.modelo.db.*;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import org.primefaces.model.chart.DonutChartModel;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 * Clase controlador de VerPerfil.
 *
 * @author Pablo Gerardo Gonzalez Lopez
 * @version 1.0
 */
@ManagedBean
//@RequestScoped
@ViewScoped
public class VisorUsuario implements Serializable {

    private final ControladorJpaUsuario jpaUsuario;
    /* ID del usuario actual. */
    private Long id;
    /* Objeto del usuario actual. */
    private Usuario usuario;
    /* Objeto que contiene la informacion estadistica del usuario.*/
    private DonutChartModel estadistica;

    /**
     *<code>VisorUsuario</code> Constructor.
     */
    public VisorUsuario() {
        FabricaControladorJpa fabricaJpa = new FabricaControladorJpa();
        this.jpaUsuario = fabricaJpa.obtenerControladorJpaUsuario();
    }

    /**
     *<code>setId</code> Método actualiza el valor del ID del usuario actual.
     *@param l tipo <code>long</code>: ID del usuario.
     */
    public void setId(Long l) {
        this.id = l;
    }

    /**
     *<code>getId</code> Método que regresa el ID del usuario actual.
     *@return tipo <code>long</code>: ID del usuario actual.
     */
    public Long getId() {
        return this.id;
    }

    /**
     * <code>obtenerUsuario</code> Método inicializa los objetos relacionados con
     * el usuario actual.
     *
     *@return tipo <code>String</code>: Dirección de redireccionamiento.
     */
    public String obtenerUsuario() {
        if (this.id == null) {
            return "index";
        }

        this.usuario = jpaUsuario.buscarPorId(this.id);
        if (this.usuario == null) {
            return "index";
        }

        this.inicializaEstadistica();
        return null;
    }

    /**
     *<code>getUsuario</code> Método que regresa el usuario actual.
     *@return tipo <code>Usuario</code>: Objeto del usuario actual.
     */
    public Usuario getUsuario() {
        return this.usuario;
    }

    public int sizeCalificaciones() {
        return usuario.getCalificaciones().size();
    }
    
    public int sizeComentarios() {
        return usuario.getComentarios().size();
    }

    public DonutChartModel getEstadistica(){
        return this.estadistica;
    }
    
    private void inicializaEstadistica() {
        this.estadistica = new DonutChartModel();
        this.estadistica.setTitle("Informacion:");
        this.estadistica.setLegendPosition("e");
        this.estadistica.setSliceMargin(5);
        this.estadistica.setShowDataLabels(true);
        this.estadistica.setDataFormat("value");
        this.estadistica.setShadow(false);
        
        
        Map<String, Integer> calificaciones = new LinkedHashMap<String, Integer>();
        calificaciones.put("0", 0);
        calificaciones.put("1", 0);
        calificaciones.put("2", 0);
        calificaciones.put("3", 0);
        calificaciones.put("4", 0);
        calificaciones.put("5", 0);
        for(Calificacion c : this.usuario.getCalificaciones()){
            switch((int)c.getCalificacion()){
                case 0: {
                    calificaciones.put("0", calificaciones.get("0") + 1);
                    break;
                }
                case 1: {
                    calificaciones.put("1", calificaciones.get("1") + 1);                    
                    break;
                }
                case 2: {
                    calificaciones.put("2", calificaciones.get("2") + 1);
                    break;
                }
                case 3: {
                    calificaciones.put("3", calificaciones.get("3") + 1);
                    break;
                }
                case 4: {
                    calificaciones.put("4", calificaciones.get("4") + 1);
                    break;
                }
                case 5: {
                    calificaciones.put("5", calificaciones.get("5") + 1);                    
                    break;
                }
            }
        }

        Map<String, Number> calificacionesC = new LinkedHashMap<String, Number>();
        calificacionesC.put("0", calificaciones.get("0"));
        calificacionesC.put("1", calificaciones.get("1"));
        calificacionesC.put("2", calificaciones.get("2"));
        calificacionesC.put("3", calificaciones.get("3"));
        calificacionesC.put("4", calificaciones.get("4"));
        calificacionesC.put("5", calificaciones.get("5"));
        estadistica.addCircle(calificacionesC);

        Map<String, Integer> reacciones = new LinkedHashMap<String, Integer>();
        reacciones.put("1", 0);
        reacciones.put("2", 0);
        for(Comentario c : this.usuario.getComentarios()){
            for(Reaccion r : c.getReacciones()){
                switch(r.getReaccion()){
                    case 1: {
                        reacciones.put("1", reacciones.get("1") + 1);
                        break;
                    }
                    case 2: {
                        reacciones.put("2", reacciones.get("2") + 1);
                        break;
                    }
                }
            }
        }

        Map<String, Number> reaccionesC = new LinkedHashMap<String, Number>();
        reaccionesC.put("👍", reacciones.get("1"));
        reaccionesC.put("👎", reacciones.get("2"));
        estadistica.addCircle(reaccionesC);
    }
}
