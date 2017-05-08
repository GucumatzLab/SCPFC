/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gucumatz.scpfc.modelo;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author lchacon
 */
@Entity
@Table(name = "puesto", catalog = "gucumatz", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"id"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Puesto.findAll", query = "SELECT p FROM Puesto p")
    , @NamedQuery(name = "Puesto.findById", query = "SELECT p FROM Puesto p WHERE p.id = :id")
    , @NamedQuery(name = "Puesto.findByNombre", query = "SELECT p FROM Puesto p WHERE p.nombre = :nombre")
    , @NamedQuery(name = "Puesto.findByTipoComida", query = "SELECT p FROM Puesto p WHERE p.tipoComida = :tipoComida")
    , @NamedQuery(name = "Puesto.findByHorario", query = "SELECT p FROM Puesto p WHERE p.horario = :horario")
    , @NamedQuery(name = "Puesto.findByUbicacion", query = "SELECT p FROM Puesto p WHERE p.ubicacion = :ubicacion")
    , @NamedQuery(name = "Puesto.findByLatitud", query = "SELECT p FROM Puesto p WHERE p.latitud = :latitud")
    , @NamedQuery(name = "Puesto.findByLongitud", query = "SELECT p FROM Puesto p WHERE p.longitud = :longitud")})
public class Puesto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic(optional = false)
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    @Basic(optional = false)
    @Column(name = "tipoComida", nullable = false, length = 200)
    private String tipoComida;
    @Column(name = "horario", length = 100)
    private String horario;
    @Column(name = "ubicacion", length = 100)
    private String ubicacion;
    @Basic(optional = false)
    @Column(name = "latitud", nullable = false)
    private double latitud;
    @Basic(optional = false)
    @Column(name = "longitud", nullable = false)
    private double longitud;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "puesto")
    private List<Calificacion> calificacionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "puesto")
    private List<FotoPuesto> fotoPuestoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "puesto")
    private List<Comentario> comentarioList;

    public Puesto() {
    }

    public Puesto(Long id) {
        this.id = id;
    }

    public Puesto(Long id, String nombre, String tipoComida, double latitud, double longitud) {
        this.id = id;
        this.nombre = nombre;
        this.tipoComida = tipoComida;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoComida() {
        return tipoComida;
    }

    public void setTipoComida(String tipoComida) {
        this.tipoComida = tipoComida;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    @XmlTransient
    public List<Calificacion> getCalificacionList() {
        return calificacionList;
    }

    public void setCalificacionList(List<Calificacion> calificacionList) {
        this.calificacionList = calificacionList;
    }

    @XmlTransient
    public List<FotoPuesto> getFotoPuestoList() {
        return fotoPuestoList;
    }

    public void setFotoPuestoList(List<FotoPuesto> fotoPuestoList) {
        this.fotoPuestoList = fotoPuestoList;
    }

    @XmlTransient
    public List<Comentario> getComentarioList() {
        return comentarioList;
    }

    public void setComentarioList(List<Comentario> comentarioList) {
        this.comentarioList = comentarioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Puesto)) {
            return false;
        }
        Puesto other = (Puesto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gucumatz.scpfc.modelo.Puesto[ id=" + id + " ]";
    }

}
