package gucumatz.scpfc.controlador;

import gucumatz.scpfc.modelo.Usuario;
import gucumatz.scpfc.modelo.db.FabricaControladorJpa;
import gucumatz.scpfc.modelo.db.ControladorJpaUsuario;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "activador")
@RequestScoped
public class ActivacionDeCuenta implements Serializable {

    private Long idUsuario;

    private String codigoDeActivacion;

    @ManagedProperty("#{sesionActiva}")
    private SesionActiva sesionActiva;

    public ActivacionDeCuenta() {
    }

    public String activarCuenta() {
        if (idUsuario != null && codigoDeActivacion != null) {
            ControladorJpaUsuario jpaUsuario
                    = new FabricaControladorJpa().obtenerControladorJpaUsuario();

            Usuario usuario = jpaUsuario.buscarPorId(idUsuario);
            if (usuario != null
                    && codigoDeActivacion.equals(usuario.getCodigoDeActivacion())) {
                usuario.setCodigoDeActivacion(null);
                usuario.setConfirmada(true);

                try {
                    jpaUsuario.editar(usuario);
                    sesionActiva.setUsuario(usuario);
                    FacesMessage mensaje
                            = new FacesMessage("Tu cuenta ha sido confirmada.");
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                } catch (Exception ex) {

                }
            }
        }

        return "index";
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCodigoDeActivacion() {
        return codigoDeActivacion;
    }

    public void setCodigoDeActivacion(String codigoDeActivacion) {
        this.codigoDeActivacion = codigoDeActivacion;
    }

    public SesionActiva getSesionActiva() {
        return sesionActiva;
    }

    public void setSesionActiva(SesionActiva sesionActiva) {
        this.sesionActiva = sesionActiva;
    }

}
