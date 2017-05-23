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
@Table(name = "usuario", catalog = "gucumatz", schema = "",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id"}),
        @UniqueConstraint(columnNames = {"correoElectronico"}),
        @UniqueConstraint(columnNames = {"nombre"}),
    })
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuario.buscarPorNombre",
        query = "SELECT u FROM Usuario u WHERE u.nombre = :nombre"),
    @NamedQuery(name = "Usuario.buscarPorCorreoElectronico",
        query = "SELECT u FROM Usuario u"
            + " WHERE u.correoElectronico = :correoElectronico"),
    @NamedQuery(name = "Usuario.buscarPorCuenta",
        query = "SELECT u FROM Usuario u"
            + " WHERE u.nombre = :cuenta OR u.correoElectronico = :cuenta"),
    })
@SuppressWarnings("checkstyle:magicnumber")
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
    @Column(name = "confirmado", nullable = false)
    private boolean confirmado;
    @Basic(optional = false)
    @Column(name = "eliminado", nullable = false)
    private boolean eliminado;
    @Column(name = "rutaImagen", length = 100)
    private String rutaImagen;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario")
    private List<Calificacion> calificaciones;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario")
    private List<Comentario> comentarios;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuarioId")
    private List<Reaccion> reacciones;

    public Usuario() {
    }

    public Usuario(Long id) {
        this.id = id;
    }

    public Usuario(Long id, String nombre,
                   String correoElectronico, String contrasena,
                   boolean esAdministrador, boolean confirmado,
                   boolean eliminado) {
        this.id = id;
        this.nombre = nombre;
        this.correoElectronico = correoElectronico;
        this.contrasena = contrasena;
        this.esAdministrador = esAdministrador;
        this.confirmado = confirmado;
        this.eliminado = eliminado;
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

    public boolean getConfirmado() {
        return confirmado;
    }

    public void setConfirmado(boolean confirmado) {
        this.confirmado = confirmado;
    }

    public boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    @XmlTransient
    public List<Calificacion> getCalificaciones() {
        return calificaciones;
    }

    public void setCalificaciones(List<Calificacion> calificaciones) {
        this.calificaciones = calificaciones;
    }

    @XmlTransient
    public List<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    @XmlTransient
    public List<Reaccion> getReacciones() {
        return reacciones;
    }

    public void setReacciones(List<Reaccion> reacciones) {
        this.reacciones = reacciones;
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
