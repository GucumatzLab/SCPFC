/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gucumatz.scpfc.controlador;

import java.io.Serializable;

import gucumatz.scpfc.modelo.db.*;
import gucumatz.scpfc.modelo.Puesto;
import java.io.IOException;

import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

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
     *<code>init</code> Método inicializa el mapa.
     */
    @PostConstruct
    public void init() {

        advancedModel = new DefaultMapModel();

        ControladorJpaPuesto jpaPuesto
            = new FabricaControladorJpa().obtenerControladorJpaPuesto();

        /*
         * Se obtienen todos los puestos y se agregan los respectivos
         * marcadores.
         */
        for (Puesto p : jpaPuesto.buscarTodos()) {
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