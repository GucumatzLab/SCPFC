/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gucumatz.scpfc.modelo;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author pablog
 */
@Entity
@Table(name = "fotosPuesto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FotosPuesto.findAll", query = "SELECT f FROM FotosPuesto f")
    , @NamedQuery(name = "FotosPuesto.findByIdPuesto", query = "SELECT f FROM FotosPuesto f WHERE f.fotosPuestoPK.idPuesto = :idPuesto")
    , @NamedQuery(name = "FotosPuesto.findByUrl", query = "SELECT f FROM FotosPuesto f WHERE f.fotosPuestoPK.url = :url")})
public class FotosPuesto implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FotosPuestoPK fotosPuestoPK;

    public FotosPuesto() {
    }

    public FotosPuesto(FotosPuestoPK fotosPuestoPK) {
        this.fotosPuestoPK = fotosPuestoPK;
    }

    public FotosPuesto(int idPuesto, String url) {
        this.fotosPuestoPK = new FotosPuestoPK(idPuesto, url);
    }

    public FotosPuestoPK getFotosPuestoPK() {
        return fotosPuestoPK;
    }

    public void setFotosPuestoPK(FotosPuestoPK fotosPuestoPK) {
        this.fotosPuestoPK = fotosPuestoPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fotosPuestoPK != null ? fotosPuestoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FotosPuesto)) {
            return false;
        }
        FotosPuesto other = (FotosPuesto) object;
        if ((this.fotosPuestoPK == null && other.fotosPuestoPK != null) || (this.fotosPuestoPK != null && !this.fotosPuestoPK.equals(other.fotosPuestoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gucumatz.scpfc.modelo.FotosPuesto[ fotosPuestoPK=" + fotosPuestoPK + " ]";
    }
    
}
