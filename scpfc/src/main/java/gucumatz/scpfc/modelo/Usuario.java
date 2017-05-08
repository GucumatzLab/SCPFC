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
@Table(name = "usuario", catalog = "gucumatz", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"id"})
    , @UniqueConstraint(columnNames = {"correoElectronico"})
    , @UniqueConstraint(columnNames = {"nombre"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u")
    , @NamedQuery(name = "Usuario.findById", query = "SELECT u FROM Usuario u WHERE u.id = :id")
    , @NamedQuery(name = "Usuario.findByNombre", query = "SELECT u FROM Usuario u WHERE u.nombre = :nombre")
    , @NamedQuery(name = "Usuario.findByCorreoElectronico", query = "SELECT u FROM Usuario u WHERE u.correoElectronico = :correoElectronico")
    , @NamedQuery(name = "Usuario.findByContrasena", query = "SELECT u FROM Usuario u WHERE u.contrasena = :contrasena")
    , @NamedQuery(name = "Usuario.findByCodigoDeActivacion", query = "SELECT u FROM Usuario u WHERE u.codigoDeActivacion = :codigoDeActivacion")
    , @NamedQuery(name = "Usuario.findByEsAdministrador", query = "SELECT u FROM Usuario u WHERE u.esAdministrador = :esAdministrador")
    , @NamedQuery(name = "Usuario.findByConfirmada", query = "SELECT u FROM Usuario u WHERE u.confirmada = :confirmada")
    , @NamedQuery(name = "Usuario.findByEliminada", query = "SELECT u FROM Usuario u WHERE u.eliminada = :eliminada")
    , @NamedQuery(name = "Usuario.findByRutaImagen", query = "SELECT u FROM Usuario u WHERE u.rutaImagen = :rutaImagen")})
public class Usuario implements Serializable {

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
    @Column(name = "correoElectronico", nullable = false, length = 100)
    private String correoElectronico;
    @Basic(optional = false)
    @Column(name = "contrasena", nullable = false, length = 100)
    private String contrasena;
    @Column(name = "codigoDeActivacion", length = 100)
    private String codigoDeActivacion;
    @Basic(optional = false)
    @Column(name = "esAdministrador", nullable = false)
    private boolean esAdministrador;
    @Basic(optional = false)
    @Column(name = "confirmada", nullable = false)
    private boolean confirmada;
    @Basic(optional = false)
    @Column(name = "eliminada", nullable = false)
    private boolean eliminada;
    @Column(name = "rutaImagen", length = 100)
    private String rutaImagen;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario")
    private List<Calificacion> calificacionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario")
    private List<Comentario> comentarioList;

    public Usuario() {
    }

    public Usuario(Long id) {
        this.id = id;
    }

    public Usuario(Long id, String nombre, String correoElectronico, String contrasena, boolean esAdministrador, boolean confirmada, boolean eliminada) {
        this.id = id;
        this.nombre = nombre;
        this.correoElectronico = correoElectronico;
        this.contrasena = contrasena;
        this.esAdministrador = esAdministrador;
        this.confirmada = confirmada;
        this.eliminada = eliminada;
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

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getCodigoDeActivacion() {
        return codigoDeActivacion;
    }

    public void setCodigoDeActivacion(String codigoDeActivacion) {
        this.codigoDeActivacion = codigoDeActivacion;
    }

    public boolean getEsAdministrador() {
        return esAdministrador;
    }

    public void setEsAdministrador(boolean esAdministrador) {
        this.esAdministrador = esAdministrador;
    }

    public boolean getConfirmada() {
        return confirmada;
    }

    public void setConfirmada(boolean confirmada) {
        this.confirmada = confirmada;
    }

    public boolean getEliminada() {
        return eliminada;
    }

    public void setEliminada(boolean eliminada) {
        this.eliminada = eliminada;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    @XmlTransient
    public List<Calificacion> getCalificacionList() {
        return calificacionList;
    }

    public void setCalificacionList(List<Calificacion> calificacionList) {
        this.calificacionList = calificacionList;
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
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gucumatz.scpfc.modelo.Usuario[ id=" + id + " ]";
    }

}
