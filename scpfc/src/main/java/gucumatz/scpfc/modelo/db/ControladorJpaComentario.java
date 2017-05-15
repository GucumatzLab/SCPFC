package gucumatz.scpfc.modelo.db;

import gucumatz.scpfc.modelo.Comentario;
import gucumatz.scpfc.modelo.Puesto;
import gucumatz.scpfc.modelo.Usuario;
import gucumatz.scpfc.modelo.db.exceptions.NonexistentEntityException;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author lchacon
 */
public class ControladorJpaComentario implements Serializable {

    private EntityManagerFactory emf = null;

    public ControladorJpaComentario(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Guarda un comentario en la base de datos.
     *
     * @param comentario el comentario que se quiere guardar.
     */
    @SuppressWarnings("checkstyle:linelength")
    public void crear(Comentario comentario) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Puesto puesto = comentario.getPuesto();
            if (puesto != null) {
                puesto = em.getReference(puesto.getClass(), puesto.getId());
                comentario.setPuesto(puesto);
            }
            Usuario usuario = comentario.getUsuario();
            if (usuario != null) {
                usuario = em.getReference(usuario.getClass(), usuario.getId());
                comentario.setUsuario(usuario);
            }
            em.persist(comentario);
            if (puesto != null) {
                puesto.getComentarios().add(comentario);
                puesto = em.merge(puesto);
            }
            if (usuario != null) {
                usuario.getComentarios().add(comentario);
                usuario = em.merge(usuario);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Modifica un comentario en la base de datos. El comentario que recibe debe
     * tener un ID correspondiente a un renglón de la base de datos y debe
     * contener los nuevos datos que se quieren guardar.
     *
     * @param comentario el comentario que se quiere editar
     * @throws NonexistentEntityException si no hay un comentario con el mismo
     * ID que {@code comentario}
     * @throws Exception si ocurre un error
     */
    @SuppressWarnings("checkstyle:linelength")
    public void editar(Comentario comentario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Comentario persistentComentario = em.find(Comentario.class, comentario.getId());
            Puesto puestoOld = persistentComentario.getPuesto();
            Puesto puestoNew = comentario.getPuesto();
            Usuario usuarioOld = persistentComentario.getUsuario();
            Usuario usuarioNew = comentario.getUsuario();
            if (puestoNew != null) {
                puestoNew = em.getReference(puestoNew.getClass(), puestoNew.getId());
                comentario.setPuesto(puestoNew);
            }
            if (usuarioNew != null) {
                usuarioNew = em.getReference(usuarioNew.getClass(), usuarioNew.getId());
                comentario.setUsuario(usuarioNew);
            }
            comentario = em.merge(comentario);
            if (puestoOld != null && !puestoOld.equals(puestoNew)) {
                puestoOld.getComentarios().remove(comentario);
                puestoOld = em.merge(puestoOld);
            }
            if (puestoNew != null && !puestoNew.equals(puestoOld)) {
                puestoNew.getComentarios().add(comentario);
                puestoNew = em.merge(puestoNew);
            }
            if (usuarioOld != null && !usuarioOld.equals(usuarioNew)) {
                usuarioOld.getComentarios().remove(comentario);
                usuarioOld = em.merge(usuarioOld);
            }
            if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
                usuarioNew.getComentarios().add(comentario);
                usuarioNew = em.merge(usuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = comentario.getId();
                if (findComentario(id) == null) {
                    throw new NonexistentEntityException("The comentario with id " + id + " no longer exists.");
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
     * Elimina un comentario de la base de datos.
     *
     * @param id el identificador del comentario que se quiere eliminar.
     * @throws NonexistentEntityException si el id no corresponde a ningun
     * comentario.
     */
    @SuppressWarnings("checkstyle:linelength")
    public void destruir(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Comentario comentario;
            try {
                comentario = em.getReference(Comentario.class, id);
                comentario.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The comentario with id " + id + " no longer exists.", enfe);
            }
            Puesto puesto = comentario.getPuesto();
            if (puesto != null) {
                puesto.getComentarios().remove(comentario);
                puesto = em.merge(puesto);
            }
            Usuario usuario = comentario.getUsuario();
            if (usuario != null) {
                usuario.getComentarios().remove(comentario);
                usuario = em.merge(usuario);
            }
            em.remove(comentario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Busca todos los comentarios registrados en la base de datos.
     *
     * @return una lista con todos los comentarios existentes.
     */
    @SuppressWarnings("checkstyle:linelength")
    public List<Comentario> findComentarioEntities() {
        return findComentarioEntities(true, -1, -1);
    }

    /**
     * Busca una cantidad limitada de comentarios en la base de datos.
     *
     * @param maxResults la máxima cantidad de comentarios a regresar
     * @param firstResult la primera posición a regresar
     * @return una lista con a lo mas {@code maxResults} comentarios iniciando a
     * partir del número {@code firstResult} de la lista completa.
     */
    @SuppressWarnings("checkstyle:linelength")
    public List<Comentario> findComentarioEntities(int maxResults, int firstResult) {
        return findComentarioEntities(false, maxResults, firstResult);
    }

    /**
     * Busca cierta cantidad de comentarios en la base de datos.
     *
     * @param all dice si se deben obtener todos los comentarios
     * @param maxResults si {@code all} es falso, limita cuántos comentarios se
     * obtienen
     * @param firstResult si {@code all} es falso, dice cuál es el primer
     * comentario que se obtiene
     * @return una lista de comentarios siguiendo las restricciones dadas por
     * los parámetros
     */
    @SuppressWarnings("checkstyle:linelength")
    private List<Comentario> findComentarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Comentario.class));
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
     * Busca un comentario por ID.
     *
     * @param id el ID del comentario deseado
     * @return el comentario con ID igual a {@code id} o null si no existe.
     */
    public Comentario findComentario(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Comentario.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Cuenta cuántos comentarios hay en la base de datos.
     *
     * @return el número de comentarios existentes
     */
    public int getComentarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Comentario> rt = cq.from(Comentario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
