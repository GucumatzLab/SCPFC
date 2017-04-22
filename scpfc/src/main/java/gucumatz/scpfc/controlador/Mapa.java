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
 * Clase para controlar el Mapa
 *
 * @author Pablo Gerardo Gonzalez Lopez
 * @version 1.0
 */
@ManagedBean
@ViewScoped
public class Mapa implements Serializable{

    private MapModel advancedModel;
    private Marker marker;

    @PostConstruct
    public void init() {
        
        advancedModel = new DefaultMapModel();
        
        PuestoJpaController jpaPuesto = new FabricaControladorJpa().obtenerControladorJpaPuesto();

        for (Puesto p : jpaPuesto.findPuestoEntities()) {
            Double latitud = p.getLatitud();
            Double longitud = p.getLongitud();
            String nombre = p.getNombre();
            advancedModel.addOverlay(new Marker(new LatLng(latitud, longitud), nombre, p));
        }
    }

    public MapModel getAdvancedModel() {
        return advancedModel;
    }

    public void onMarkerSelect(OverlaySelectEvent event) {
        marker = (Marker) event.getOverlay();
    }

    public Marker getMarker() {
        return marker;
    }
    
}