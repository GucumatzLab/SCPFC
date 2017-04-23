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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author lchacon
 */
@Entity
@Table(name = "fotospuesto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Fotospuesto.findAll", query = "SELECT f FROM Fotospuesto f")
    , @NamedQuery(name = "Fotospuesto.findById", query = "SELECT f FROM Fotospuesto f WHERE f.id = :id")
    , @NamedQuery(name = "Fotospuesto.findByPuestoId", query = "SELECT f FROM Fotospuesto f WHERE f.puestoId = :puestoId")
    , @NamedQuery(name = "Fotospuesto.findByUrl", query = "SELECT f FROM Fotospuesto f WHERE f.url = :url")})
public class Fotospuesto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "url")
    private String url;
    @JoinColumn(name = "puesto_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Puesto puestoId;

    public Fotospuesto() {
    }

    public Fotospuesto(Long id) {
        this.id = id;
    }

    public Fotospuesto(Long id, String url) {
        this.id = id;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Puesto getPuestoId() {
        return puestoId;
    }

    public void setPuestoId(Puesto puestoId) {
        this.puestoId = puestoId;
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
        if (!(object instanceof Fotospuesto)) {
            return false;
        }
        Fotospuesto other = (Fotospuesto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gucumatz.scpfc.modelo.Fotospuesto[ id=" + id + " ]";
    }
    
}
