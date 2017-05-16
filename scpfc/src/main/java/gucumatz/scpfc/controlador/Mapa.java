package gucumatz.scpfc.controlador;

import gucumatz.scpfc.modelo.Puesto;
import gucumatz.scpfc.modelo.db.*;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

/**
 * Clase controlador del mapa.
 *
 * @author Pablo Gerardo Gonzalez Lopez
 * @version 1.0
 */
@ManagedBean
@ViewScoped
public class Mapa implements Serializable {

    private MapModel advancedModel;
    /* El marcador del mapa que esta seleccionado. */
    private Marker marker;

    /**
     * Identificador de un puesto que se ignora al poner los marcadores.
     */
    private Long idPuestoIgnorado;

    /**
     *<code>init</code> Método inicializa el mapa.
     */
    public void init() {

        advancedModel = new DefaultMapModel();

        ControladorJpaPuesto jpaPuesto
            = new FabricaControladorJpa().obtenerControladorJpaPuesto();

        /*
         * Se obtienen todos los puestos y se agregan los respectivos
         * marcadores.
         */
        for (Puesto p : jpaPuesto.buscarTodos()) {
            /* Omite al puesto ignorado. */
            if (p.getId().equals(idPuestoIgnorado)) {
                continue;
            }

            Double latitud = p.getLatitud();
            Double longitud = p.getLongitud();
            String nombre = p.getNombre();
            Marker marcador
                = new Marker(new LatLng(latitud, longitud), nombre, p);
            advancedModel.addOverlay(marcador);
        }
    }

    /**
     *<code>getAdvancedModel</code>.
     *@return tipo <code>MapModel</code>.
     */
    public MapModel getAdvancedModel() {
        return advancedModel;
    }

    public Long getIdPuestoIgnorado() {
        return idPuestoIgnorado;
    }

    public void setIdPuestoIgnorado(Long idPuestoIgnorado) {
        this.idPuestoIgnorado = idPuestoIgnorado;
    }

    /**
     * <code>onMarkerSelect</code> Método que se invoca cuando se hace click en
     * un marcador del mapa para mostrar la información del puesto.
     *
     * @param event tipo <code>OverlaySelectEvent</code>: Evento que invocó al
     * método.
     */
    public void onMarkerSelect(OverlaySelectEvent event) {
        marker = (Marker) event.getOverlay();
    }

    /**
     *<code>getMarker</code> Método que regresa el marcador actual.
     *@return tipo <code>Marker</code>: El marcador actual.
     */
    public Marker getMarker() {
        return marker;
    }

}