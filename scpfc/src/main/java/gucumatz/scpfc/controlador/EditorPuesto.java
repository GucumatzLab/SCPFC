package gucumatz.scpfc.controlador;

import gucumatz.scpfc.modelo.Puesto;
import gucumatz.scpfc.modelo.db.ControladorJpaFotoPuesto;
import gucumatz.scpfc.modelo.db.ControladorJpaPuesto;
import gucumatz.scpfc.modelo.db.FabricaControladorJpa;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author lchacon
 */
@ManagedBean
@ViewScoped
public class EditorPuesto implements Serializable {

    private final ControladorJpaPuesto jpaPuesto;
    private final ControladorJpaFotoPuesto jpaFotoPuesto;

    /**
     * ID del puesto a editar.
     */
    private Long idPuesto;

    /**
     * Puesto a editar.
     */
    private Puesto puesto;

    /**
     * Crea una nueva instancia de EditorPuesto
     */
    public EditorPuesto() {
        FabricaControladorJpa fabricaJpa = new FabricaControladorJpa();
        jpaPuesto = fabricaJpa.obtenerControladorJpaPuesto();
        jpaFotoPuesto = fabricaJpa.obtenerControladorJpaFotoPuesto();
    }

    public Long getIdPuesto() {
        return this.idPuesto;
    }

    public void setIdPuesto(Long idPuesto) {
        this.idPuesto = idPuesto;
    }

    public Puesto getPuesto() {
        return this.puesto;
    }

    /**
     * Inicializa el Bean buscando el puesto con el ID recibido. Si no se
     * recibió un ID o el puesto no existe, redirige a la página principal.
     *
     * @return dirección a la que redirige, o null si no es necesario
     */
    public String preparar() {
        if (idPuesto != null) {
            puesto = jpaPuesto.buscarPorId(idPuesto);

            if (puesto != null) {
                /* Todo bien, permanece en la página. */
                return null;
            }
        }

        return "index?faces-redirect=true";
    }

    /**
     * Actualiza los datos del puesto.
     *
     * @return la página a la que redirige
     */
    public String actualizar() {
        try {
            jpaPuesto.editar(puesto);
        } catch (Exception e) {

        }
        return "DetallesPuesto?faces-redirect=true&includeViewParams=true";
    }

}
