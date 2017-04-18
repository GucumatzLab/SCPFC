/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gucumatz.scpfc.modelo;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Moctezuma19
 */
@Entity
@Table(name = "fotospuesto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Fotospuesto.findAll", query = "SELECT f FROM Fotospuesto f")
    , @NamedQuery(name = "Fotospuesto.findByIdPuesto", query = "SELECT f FROM Fotospuesto f WHERE f.fotospuestoPK.idPuesto = :idPuesto")
    , @NamedQuery(name = "Fotospuesto.findByUrl", query = "SELECT f FROM Fotospuesto f WHERE f.fotospuestoPK.url = :url")})
public class Fotospuesto implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FotospuestoPK fotospuestoPK;
    @JoinColumn(name = "idPuesto", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Puesto puesto;

    public Fotospuesto() {
    }

    public Fotospuesto(FotospuestoPK fotospuestoPK) {
        this.fotospuestoPK = fotospuestoPK;
    }

    public Fotospuesto(long idPuesto, String url) {
        this.fotospuestoPK = new FotospuestoPK(idPuesto, url);
    }

    public FotospuestoPK getFotospuestoPK() {
        return fotospuestoPK;
    }

    public void setFotospuestoPK(FotospuestoPK fotospuestoPK) {
        this.fotospuestoPK = fotospuestoPK;
    }

    public Puesto getPuesto() {
        return puesto;
    }

    public void setPuesto(Puesto puesto) {
        this.puesto = puesto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fotospuestoPK != null ? fotospuestoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Fotospuesto)) {
            return false;
        }
        Fotospuesto other = (Fotospuesto) object;
        if ((this.fotospuestoPK == null && other.fotospuestoPK != null) || (this.fotospuestoPK != null && !this.fotospuestoPK.equals(other.fotospuestoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gucumatz.scpfc.modelo.Fotospuesto[ fotospuestoPK=" + fotospuestoPK + " ]";
    }
    
}