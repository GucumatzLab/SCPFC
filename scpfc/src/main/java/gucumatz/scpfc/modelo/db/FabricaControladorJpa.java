package gucumatz.scpfc.modelo.db;

import java.io.Serializable;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author lchacon
 */
public class FabricaControladorJpa implements Serializable {

    private final EntityManagerFactory emf;

    public FabricaControladorJpa() {
        this.emf = Persistence.createEntityManagerFactory("SCPFC-PU");
    }

    /**
     * Genera un controlador JPA para la entidad Comentario.
     *
     * @return un controlador JPA para Comentario.
     */
    public ControladorJpaComentario obtenerControladorJpaComentario() {
        return new ControladorJpaComentario(emf);
    }

    /**
     * Genera un controlador JPA para la entidad Calificacion.
     *
     * @return un controlador JPA para Calificacion.
     */
    public ControladorJpaCalificacion obtenerControladorJpaCalificacion() {
        return new ControladorJpaCalificacion(emf);
    }

    /**
     * Genera un controlador JPA para la entidad Usuario.
     *
     * @return un controlador JPA para Usuario.
     */
    public ControladorJpaUsuario obtenerControladorJpaUsuario() {
        return new ControladorJpaUsuario(emf);
    }

    /**
     * Genera un controlador JPA para la entidad Puesto.
     *
     * @return un controlador JPA para Puesto.
     */
    public ControladorJpaPuesto obtenerControladorJpaPuesto() {
        return new ControladorJpaPuesto(emf);
    }

    /**
     * Genera un controlador JPA para la entidad FotoPuesto.
     *
     * @return un controlador JPA para FotoPuesto.
     */
    public ControladorJpaFotoPuesto obtenerControladorJpaFotoPuesto() {
        return new ControladorJpaFotoPuesto(emf);
    }

    /**
     * Genera un controlador JPA para la entidad Reaccion.
     *
     * @return un controlador JPA para Reaccion.
     */
    public ControladorJpaReaccion obtenerControladorJpaReaccion() {
        return new ControladorJpaReaccion(emf);
    }
}
