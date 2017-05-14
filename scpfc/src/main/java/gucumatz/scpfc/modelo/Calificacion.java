/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gucumatz.scpfc.modelo;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author lchacon
 */
@Entity
@Table(name = "calificacion", catalog = "gucumatz", schema = "",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Calificacion.buscarPorPuesto",
            query = "SELECT c FROM Calificacion c WHERE c.puesto = :puesto")
    , @NamedQuery(name = "Calificacion.buscarPorUsuarioYPuesto",
            query = "SELECT c FROM Calificacion c"
                + " WHERE c.usuario = :usuario AND c.puesto = :puesto")
    , @NamedQuery(name = "Calificacion.promedioDePuesto",
            query = "SELECT AVG(c.calificacion) FROM Calificacion c"
                + " WHERE c.puesto = :puesto")
})
@SuppressWarnings("checkstyle:magicnumber")
public class Calificacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic(optional = false)
    @Column(name = "calificacion", nullable = false)
    private float calificacion;
    @JoinColumn(name = "puesto_id",
        referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Puesto puesto;
    @JoinColumn(name = "usuario_id",
        referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Usuario usuario;

    public Calificacion() {
    }

    public Calificacion(Long id) {
        this.id = id;
    }

    public Calificacion(Long id, float calificacion) {
        this.id = id;
        this.calificacion = calificacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(float calificacion) {
        this.calificacion = calificacion;
    }

    public Puesto getPuesto() {
        return puesto;
    }

    public void setPuesto(Puesto puesto) {
        this.puesto = puesto;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    @SuppressWarnings("checkstyle:linelength")
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Calificacion)) {
            return false;
        }
        Calificacion other = (Calificacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gucumatz.scpfc.modelo.Calificacion[ id=" + id + " ]";
    }

}
