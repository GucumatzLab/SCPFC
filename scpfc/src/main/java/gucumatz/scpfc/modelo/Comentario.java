package gucumatz.scpfc.modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author lchacon
 */
@Entity
@Table(name = "comentario", catalog = "gucumatz", schema = "",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id"})
    })
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Comentario.buscarPorPuesto",
            query = "SELECT c FROM Comentario c WHERE c.puesto = :puesto"),
    @NamedQuery(name = "Comentario.buscarPorUsuario",
            query = "SELECT c FROM Comentario c WHERE c.usuario = :usuario"),
    })
@SuppressWarnings("checkstyle:magicnumber")
public class Comentario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic(optional = false)
    @Column(name = "comentario", nullable = false, length = 1024)
    private String comentario;
    @Basic(optional = false)
    @Column(name = "fecha", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @JoinColumn(name = "puesto_id",
        referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Puesto puesto;
    @JoinColumn(name = "usuario_id",
        referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Usuario usuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "comentarioId")
    private List<Reaccion> reacciones;

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

    @XmlTransient
    public List<Reaccion> getReacciones() {
        return reacciones;
    }

    public void setReacciones(List<Reaccion> reacciones) {
        this.reacciones = reacciones;
    }

    /**
     *<code>getReaccionSize</code> Método que regresa el numero de reacciones
     *del tipo reaccino que tiene el comentario.
     *@param reaccion tipo <code>int</code>: Reaccion a verificar.
     *@return tipo <code>int</code>: Numero de ocurrencias de esa reaccion en el
     *comentario.
     */
    public int getReaccionSize(int reaccion) {
        int c = 0;
        for (Reaccion r : reacciones) {
            if (r.getReaccion() == reaccion) {
                c++;
            }
        }

        return c;
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
