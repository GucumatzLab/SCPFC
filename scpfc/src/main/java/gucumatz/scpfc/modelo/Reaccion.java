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
 * @author pablog
 */
@Entity
@Table(name = "reaccion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Reaccion.findAll", query = "SELECT r FROM Reaccion r")
    , @NamedQuery(name = "Reaccion.findById", query = "SELECT r FROM Reaccion r WHERE r.id = :id")
    , @NamedQuery(name = "Reaccion.findByReaccion", query = "SELECT r FROM Reaccion r WHERE r.reaccion = :reaccion")})
public class Reaccion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "reaccion")
    private int reaccion;
    @JoinColumn(name = "comentario_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Comentario comentarioId;
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuario usuarioId;

    public Reaccion() {
    }

    public Reaccion(Long id) {
        this.id = id;
    }

    public Reaccion(Long id, int reaccion) {
        this.id = id;
        this.reaccion = reaccion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getReaccion() {
        return reaccion;
    }

    public void setReaccion(int reaccion) {
        this.reaccion = reaccion;
    }

    public Comentario getComentarioId() {
        return comentarioId;
    }

    public void setComentarioId(Comentario comentarioId) {
        this.comentarioId = comentarioId;
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
        if (!(object instanceof Reaccion)) {
            return false;
        }
        Reaccion other = (Reaccion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gucumatz.scpfc.modelo.Reaccion[ id=" + id + " ]";
    }
    
}
