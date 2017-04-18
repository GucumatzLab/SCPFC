/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gucumatz.scpfc.controlador;

import java.util.List;

import gucumatz.scpfc.modelo.db.*;
import gucumatz.scpfc.modelo.Puesto;

import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Clase para controlar el Mapa
 *
 * @author Pablo Gerardo Gonzalez Lopez
 * @version 1.0
 */
@ManagedBean
@ViewScoped
public class Mapa {

    private MapModel advancedModel;
    private Marker marker;

    @PostConstruct
    public void init() {
        
        advancedModel = new DefaultMapModel();
        
        PuestoJpaController jpaPuesto = new FabricaControladorJpa().obtenerControladorJpaPuesto();

        for (Puesto p : jpaPuesto.findPuestoEntities()) {
            Double latitud = new Double(p.getLatitud());
            Double longitud = new Double(p.getLongitud());
            String nombre = p.getNombre();
            System.out.println(latitud + ", " + longitud + ", " + nombre);
            advancedModel.addOverlay(new Marker(new LatLng(latitud, longitud), nombre));
        }
    }

    public MapModel getAdvancedModel() {
        return advancedModel;
    }

    public void onMarkerSelect(OverlaySelectEvent event) {
        marker = (Marker) event.getOverlay();
        System.out.println(marker.getTitle());
    }

    public Marker getMarker() {
        return marker;
    }

    public void addMarker(Double lat, Double lng, String nombre) {
        Marker marker = new Marker(new LatLng(lat, lng), nombre);
        System.out.println(lat + ", " + lng + ", " + nombre);
        marker.setIcon("http://icons.iconarchive.com/icons/icons-land/vista-map-markers/256/Map-Marker-Marker-Outside-Chartreuse-icon.png");
        advancedModel.addOverlay(marker);
    }

}