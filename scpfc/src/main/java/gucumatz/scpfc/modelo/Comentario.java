/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gucumatz.scpfc.modelo;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jaz
 */
@Entity
@Table(name = "comentario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Comentario.findAll", query = "SELECT c FROM Comentario c")
    , @NamedQuery(name = "Comentario.findById", query = "SELECT c FROM Comentario c WHERE c.id = :id")
    , @NamedQuery(name = "Comentario.findByComentario", query = "SELECT c FROM Comentario c WHERE c.comentario = :comentario")
    , @NamedQuery(name = "Comentario.findByFecha", query = "SELECT c FROM Comentario c WHERE c.fecha = :fecha")
    , @NamedQuery(name = "Comentario.findByPuesto", query = "SELECT c FROM Comentario c WHERE c.puestoId = :puestoID")})
public class Comentario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "comentario")
    private String comentario;
    @Basic(optional = false)
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @JoinColumn(name = "puesto_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Puesto puestoId;
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuario usuarioId;

    public Comentario() {
    }

    public Comentario(Long id) {
        this.id = id;
    }

    public Comentario(Long id, String comentario, Date fecha) {
        this.id = id;
        this.comentario = comentario;
        this.fecha = fecha;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Puesto getPuestoId() {
        return puestoId;
    }

    public void setPuestoId(Puesto puestoId) {
        this.puestoId = puestoId;
    }

    public Usuario getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Usuario usuarioId) {
        this.usuarioId = usuarioId;
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
        if (!(object instanceof Comentario)) {
            return false;
        }
        Comentario other = (Comentario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gucumatz.scpfc.modelo.Comentario[ id=" + id + " ]";
    }
    
}
