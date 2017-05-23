/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gucumatz.scpfc.modelo.db;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import gucumatz.scpfc.modelo.Comentario;
import gucumatz.scpfc.modelo.Reaccion;
import gucumatz.scpfc.modelo.Usuario;
import gucumatz.scpfc.modelo.db.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author pablog
 */
public class ControladorJpaReaccion implements Serializable {

    private EntityManagerFactory emf = null;
    
    public ControladorJpaReaccion(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Guarda una reaccion en la base de datos.
     *
     * @param reaccion la reaccion que se quiere guardar.
     */
    @SuppressWarnings("checkstyle:linelength")
    public void crear(Reaccion reaccion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Comentario comentarioId = reaccion.getComentarioId();
            if (comentarioId != null) {
                comentarioId = em.getReference(comentarioId.getClass(), comentarioId.getId());
                reaccion.setComentarioId(comentarioId);
            }
            Usuario usuarioId = reaccion.getUsuarioId();
            if (usuarioId != null) {
                usuarioId = em.getReference(usuarioId.getClass(), usuarioId.getId());
                reaccion.setUsuarioId(usuarioId);
            }
            em.persist(reaccion);
            if (comentarioId != null) {
                comentarioId.getReacciones().add(reaccion);
                comentarioId = em.merge(comentarioId);
            }
            if (usuarioId != null) {
                usuarioId.getReacciones().add(reaccion);
                usuarioId = em.merge(usuarioId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Modifica una reaccion en la base de datos. La reaccion que recibe debe
     * tener un ID correspondiente a un renglón de la base de datos y debe
     * contener los nuevos datos que se quieren guardar.
     *
     * @param reaccion la reaccion que se quiere editar
     * @throws NonexistentEntityException si no hay una reaccion con el mismo
     * ID que {@code reaccion}
     * @throws Exception si ocurre un error
     */
    @SuppressWarnings("checkstyle:linelength")
    public void editar(Reaccion reaccion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Reaccion persistentReaccion = em.find(Reaccion.class, reaccion.getId());
            Comentario comentarioIdOld = persistentReaccion.getComentarioId();
            Comentario comentarioIdNew = reaccion.getComentarioId();
            Usuario usuarioIdOld = persistentReaccion.getUsuarioId();
            Usuario usuarioIdNew = reaccion.getUsuarioId();
            if (comentarioIdNew != null) {
                comentarioIdNew = em.getReference(comentarioIdNew.getClass(), comentarioIdNew.getId());
                reaccion.setComentarioId(comentarioIdNew);
            }
            if (usuarioIdNew != null) {
                usuarioIdNew = em.getReference(usuarioIdNew.getClass(), usuarioIdNew.getId());
                reaccion.setUsuarioId(usuarioIdNew);
            }
            reaccion = em.merge(reaccion);
            if (comentarioIdOld != null && !comentarioIdOld.equals(comentarioIdNew)) {
                comentarioIdOld.getReacciones().remove(reaccion);
                comentarioIdOld = em.merge(comentarioIdOld);
            }
            if (comentarioIdNew != null && !comentarioIdNew.equals(comentarioIdOld)) {
                comentarioIdNew.getReacciones().add(reaccion);
                comentarioIdNew = em.merge(comentarioIdNew);
            }
            if (usuarioIdOld != null && !usuarioIdOld.equals(usuarioIdNew)) {
                usuarioIdOld.getReacciones().remove(reaccion);
                usuarioIdOld = em.merge(usuarioIdOld);
            }
            if (usuarioIdNew != null && !usuarioIdNew.equals(usuarioIdOld)) {
                usuarioIdNew.getReacciones().add(reaccion);
                usuarioIdNew = em.merge(usuarioIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = reaccion.getId();
                if (findReaccion(id) == null) {
                    throw new NonexistentEntityException("The reaccion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Elimina una reaccion de la base de datos.
     *
     * @param id el identificador de la reaccion que se quiere eliminar.
     * @throws NonexistentEntityException si el id no corresponde a ninguna
     * reaccion.
     */
    @SuppressWarnings("checkstyle:linelength")
    public void destruir(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Reaccion reaccion;
            try {
                reaccion = em.getReference(Reaccion.class, id);
                reaccion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The reaccion with id " + id + " no longer exists.", enfe);
            }
            Comentario comentarioId = reaccion.getComentarioId();
            if (comentarioId != null) {
                comentarioId.getReacciones().remove(reaccion);
                comentarioId = em.merge(comentarioId);
            }
            Usuario usuarioId = reaccion.getUsuarioId();
            if (usuarioId != null) {
                usuarioId.getReacciones().remove(reaccion);
                usuarioId = em.merge(usuarioId);
            }
            em.remove(reaccion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    
    /**
     * Busca todas las reacciones registrados en la base de datos.
     *
     * @return una lista con todos los comentarios existentes.
     */
    @SuppressWarnings("checkstyle:linelength")
    public List<Reaccion> findReaccionEntities() {
        return findReaccionEntities(true, -1, -1);
    }

    /**
     * Busca una cantidad limitada de reacciones en la base de datos.
     *
     * @param maxResults la máxima cantidad de reacciones a regresar
     * @param firstResult la primera posición a regresar
     * @return una lista con a lo mas {@code maxResults} reacciones iniciando a
     * partir del número {@code firstResult} de la lista completa.
     */
    @SuppressWarnings("checkstyle:linelength")
    public List<Reaccion> findReaccionEntities(int maxResults, int firstResult) {
        return findReaccionEntities(false, maxResults, firstResult);
    }

    /**
     * Busca cierta cantidad de reacciones en la base de datos.
     *
     * @param all dice si se deben obtener todas las reacciones
     * @param maxResults si {@code all} es falso, limita cuántas reacciones se
     * obtienen
     * @param firstResult si {@code all} es falso, dice cuál es la primer
     * reaccion que se obtiene
     * @return una lista de reacciones siguiendo las restricciones dadas por
     * los parámetros
     */
    @SuppressWarnings("checkstyle:linelength")
    private List<Reaccion> findReaccionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Reaccion.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Busca una reaccinon por ID.
     *
     * @param id el ID de la reaccion deseado
     * @return la reaccion con ID igual a {@code id} o null si no existe.
     */
    public Reaccion findReaccion(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Reaccion.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Cuenta cuántas reacciones hay en la base de datos.
     *
     * @return el número de reacciones existentes
     */
    public int getReaccionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Reaccion> rt = cq.from(Reaccion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
