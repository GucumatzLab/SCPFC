/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gucumatz.scpfc.modelo;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author pablog
 */
@Embeddable
public class FotosPuestoPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "idPuesto")
    private int idPuesto;
    @Basic(optional = false)
    @Column(name = "url")
    private String url;

    public FotosPuestoPK() {
    }

    public FotosPuestoPK(int idPuesto, String url) {
        this.idPuesto = idPuesto;
        this.url = url;
    }

    public int getIdPuesto() {
        return idPuesto;
    }

    public void setIdPuesto(int idPuesto) {
        this.idPuesto = idPuesto;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idPuesto;
        hash += (url != null ? url.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FotosPuestoPK)) {
            return false;
        }
        FotosPuestoPK other = (FotosPuestoPK) object;
        if (this.idPuesto != other.idPuesto) {
            return false;
        }
        if ((this.url == null && other.url != null) || (this.url != null && !this.url.equals(other.url))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gucumatz.scpfc.modelo.FotosPuestoPK[ idPuesto=" + idPuesto + ", url=" + url + " ]";
    }
    
}
