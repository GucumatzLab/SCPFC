package gucumatz.scpfc.modelo.db;

import gucumatz.scpfc.modelo.Calificacion;
import gucumatz.scpfc.modelo.Puesto;
import gucumatz.scpfc.modelo.Usuario;
import gucumatz.scpfc.modelo.db.exceptions.NonexistentEntityException;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author lchacon
 */
public class ControladorJpaCalificacion implements Serializable {

    private EntityManagerFactory emf = null;

    public ControladorJpaCalificacion(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Guarda una calificación en la base de datos.
     * @param calificacion la calificación que se quiere guardar.
     */
    @SuppressWarnings("checkstyle:linelength")
    public void crear(Calificacion calificacion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Puesto puesto = calificacion.getPuesto();
            if (puesto != null) {
                puesto = em.getReference(puesto.getClass(), puesto.getId());
                calificacion.setPuesto(puesto);
            }
            Usuario usuario = calificacion.getUsuario();
            if (usuario != null) {
                usuario = em.getReference(usuario.getClass(), usuario.getId());
                calificacion.setUsuario(usuario);
            }
            em.persist(calificacion);
            if (puesto != null) {
                puesto.getCalificaciones().add(calificacion);
                puesto = em.merge(puesto);
            }
            if (usuario != null) {
                usuario.getCalificaciones().add(calificacion);
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
     * Modifica una calificación en la base de datos. La calificación que recibe
     * debe tener un ID correspondiente a un renglón de la base de datos y debe
     * contener los nuevos datos que se quieren guardar.
     *
     * @param calificacion la calificación que se quiere editar
     * @throws NonexistentEntityException si no hay una calificación con el
     * mismo ID que {@code calificacion}
     * @throws Exception
     */
    @SuppressWarnings("checkstyle:linelength")
    public void editar(Calificacion calificacion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Calificacion persistentCalificacion = em.find(Calificacion.class, calificacion.getId());
            Puesto puestoOld = persistentCalificacion.getPuesto();
            Puesto puestoNew = calificacion.getPuesto();
            Usuario usuarioOld = persistentCalificacion.getUsuario();
            Usuario usuarioNew = calificacion.getUsuario();
            if (puestoNew != null) {
                puestoNew = em.getReference(puestoNew.getClass(), puestoNew.getId());
                calificacion.setPuesto(puestoNew);
            }
            if (usuarioNew != null) {
                usuarioNew = em.getReference(usuarioNew.getClass(), usuarioNew.getId());
                calificacion.setUsuario(usuarioNew);
            }
            calificacion = em.merge(calificacion);
            if (puestoOld != null && !puestoOld.equals(puestoNew)) {
                puestoOld.getCalificaciones().remove(calificacion);
                puestoOld = em.merge(puestoOld);
            }
            if (puestoNew != null && !puestoNew.equals(puestoOld)) {
                puestoNew.getCalificaciones().add(calificacion);
                puestoNew = em.merge(puestoNew);
            }
            if (usuarioOld != null && !usuarioOld.equals(usuarioNew)) {
                usuarioOld.getCalificaciones().remove(calificacion);
                usuarioOld = em.merge(usuarioOld);
            }
            if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
                usuarioNew.getCalificaciones().add(calificacion);
                usuarioNew = em.merge(usuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = calificacion.getId();
                if (buscarPorId(id) == null) {
                    throw new NonexistentEntityException("The calificacion with id " + id + " no longer exists.");
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
     * Elimina una calificación de la base de datos.
     *
     * @param id el identificador de la calificación que se quiere eliminar.
     * @throws NonexistentEntityException si el id no corresponde a ninguna
     * calificación.
     */
    @SuppressWarnings("checkstyle:linelength")
    public void destruir(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Calificacion calificacion;
            try {
                calificacion = em.getReference(Calificacion.class, id);
                calificacion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The calificacion with id " + id + " no longer exists.", enfe);
            }
            Puesto puesto = calificacion.getPuesto();
            if (puesto != null) {
                puesto.getCalificaciones().remove(calificacion);
                puesto = em.merge(puesto);
            }
            Usuario usuario = calificacion.getUsuario();
            if (usuario != null) {
                usuario.getCalificaciones().remove(calificacion);
                usuario = em.merge(usuario);
            }
            em.remove(calificacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Busca todas las calificaciones registradas en la base de datos.
     *
     * @return una lista con todas las calificaciones existentes.
     */
    public List<Calificacion> buscarTodos() {
        return findCalificacionEntities(true, -1, -1);
    }

    /**
     * Busca una cantidad limitada de calificaciones en la base de datos.
     *
     * @param maxResults la máxima cantidad de calificaciones a regresar
     * @param firstResult la primera posición a regresar
     * @return una lista con a lo mas {@code maxResults} calificaciones
     * iniciando a partir del número {@code firstResult} de la lista completa.
     */
    @SuppressWarnings("checkstyle:linelength")
    public List<Calificacion> findCalificacionEntities(int maxResults, int firstResult) {
        return findCalificacionEntities(false, maxResults, firstResult);
    }

    /**
     * Busca cierta cantidad de calificaciones en la base de datos.
     *
     * @param all dice si se deben obtener todas las calificaciones
     * @param maxResults si {@code all} es falso, limita cuántas calificaciones
     * se obtienen
     * @param firstResult si {@code all} es falso, dice cuál es la primer
     * calificación que se obtiene
     * @return una lista de calificaciones siguiendo las restricciones dadas por
     * los parámetros
     */
    @SuppressWarnings("checkstyle:linelength")
    private List<Calificacion> findCalificacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Calificacion.class));
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
     * Busca una calificación por ID.
     *
     * @param id el ID de la calificación deseada
     * @return la calificación con ID igual a {@code id} o null si no existe.
     */
    public Calificacion buscarPorId(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Calificacion.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Cuenta cuántas calificaciones hay en la base de datos.
     *
     * @return el número de calificaciones existentes
     */
    public int getCalificacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Calificacion> rt = cq.from(Calificacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    /**
     * Busca la calificación que hizo un usuario sobre un puesto específico.
     *
     * @param u el usuario que hizo la calificación
     * @param p un puesto
     * @return la calificación que el usuario le dio al puesto, o null si aún no
     * lo califica.
     */
    public Calificacion buscarPorUsuarioYPuesto(Usuario u, Puesto p) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Calificacion> query
                = em.createNamedQuery("Calificacion.buscarPorUsuarioYPuesto",
                    Calificacion.class);
            query.setParameter("usuario", u);
            query.setParameter("puesto", p);
            try {
                Calificacion result = query.getSingleResult();
                return result;
            } catch (NoResultException nre) {
                return null;
            }
        } finally {
            em.close();
        }
    }

    /**
     * Calcula la calificación promedio de un puesto.
     *
     * @param puesto el puesto del que se quiere conocer la calificación
     * promedio.
     * @return la calificación promedio de {@code puesto}, 0 si aún no tiene
     * calificaciones.
     */
    public double promedioDePuesto(Puesto puesto) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Double> query
                = em.createNamedQuery("Calificacion.promedioDePuesto",
                    Double.class);
            query.setParameter("puesto", puesto);
            return query.getSingleResult();
        } catch (Exception e) {
            return 0;
        } finally {
            em.close();
        }
    }
}
