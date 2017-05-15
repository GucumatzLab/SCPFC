package gucumatz.scpfc.modelo.db;

import gucumatz.scpfc.modelo.FotoPuesto;
import gucumatz.scpfc.modelo.Puesto;
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
public class ControladorJpaFotoPuesto implements Serializable {

    private EntityManagerFactory emf = null;

    public ControladorJpaFotoPuesto(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Guarda un objeto FotoPuesto en la base de datos.
     *
     * @param fotoPuesto el objeto que se quiere guardar.
     */
    @SuppressWarnings("checkstyle:linelength")
    public void crear(FotoPuesto fotoPuesto) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Puesto puesto = fotoPuesto.getPuesto();
            if (puesto != null) {
                puesto = em.getReference(puesto.getClass(), puesto.getId());
                fotoPuesto.setPuesto(puesto);
            }
            em.persist(fotoPuesto);
            if (puesto != null) {
                puesto.getFotosPuesto().add(fotoPuesto);
                puesto = em.merge(puesto);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Modifica un FotoPuesto en la base de datos. El objeto que recibe debe
     * tener un ID correspondiente a un renglón de la base de datos y debe
     * contener los nuevos datos que se quieren guardar.
     *
     * @param fotoPuesto el objeto que se quiere editar
     * @throws NonexistentEntityException si no hay una entrada con el mismo
     * ID que {@code comentario}
     * @throws Exception
     */
    @SuppressWarnings("checkstyle:linelength")
    public void editar(FotoPuesto fotoPuesto) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FotoPuesto persistentFotoPuesto = em.find(FotoPuesto.class, fotoPuesto.getId());
            Puesto puestoOld = persistentFotoPuesto.getPuesto();
            Puesto puestoNew = fotoPuesto.getPuesto();
            if (puestoNew != null) {
                puestoNew = em.getReference(puestoNew.getClass(), puestoNew.getId());
                fotoPuesto.setPuesto(puestoNew);
            }
            fotoPuesto = em.merge(fotoPuesto);
            if (puestoOld != null && !puestoOld.equals(puestoNew)) {
                puestoOld.getFotosPuesto().remove(fotoPuesto);
                puestoOld = em.merge(puestoOld);
            }
            if (puestoNew != null && !puestoNew.equals(puestoOld)) {
                puestoNew.getFotosPuesto().add(fotoPuesto);
                puestoNew = em.merge(puestoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = fotoPuesto.getId();
                if (findFotoPuesto(id) == null) {
                    throw new NonexistentEntityException("The fotoPuesto with id " + id + " no longer exists.");
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
     * Elimina una foto de la base de datos.
     *
     * @param id el identificador de la foto que se quiere eliminar.
     * @throws NonexistentEntityException si el id no corresponde a ninguna
     * foto.
     */
    @SuppressWarnings("checkstyle:linelength")
    public void destruir(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FotoPuesto fotoPuesto;
            try {
                fotoPuesto = em.getReference(FotoPuesto.class, id);
                fotoPuesto.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The fotoPuesto with id " + id + " no longer exists.", enfe);
            }
            Puesto puesto = fotoPuesto.getPuesto();
            if (puesto != null) {
                puesto.getFotosPuesto().remove(fotoPuesto);
                puesto = em.merge(puesto);
            }
            em.remove(fotoPuesto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Busca todas las fotos de puestos registradas en la base de datos.
     *
     * @return una lista con todos los objetos FotoPuesto existentes.
     */
    @SuppressWarnings("checkstyle:linelength")
    public List<FotoPuesto> findFotoPuestoEntities() {
        return findFotoPuestoEntities(true, -1, -1);
    }

    /**
     * Busca una cantidad limitada de fotos en la base de datos.
     *
     * @param maxResults la máxima cantidad de objetos a regresar
     * @param firstResult la primera posición a regresar
     * @return una lista con a lo mas {@code maxResults} fotos iniciando a
     * partir del número {@code firstResult} de la lista completa.
     */
    @SuppressWarnings("checkstyle:linelength")
    public List<FotoPuesto> findFotoPuestoEntities(int maxResults, int firstResult) {
        return findFotoPuestoEntities(false, maxResults, firstResult);
    }

    /**
     * Busca cierta cantidad de fotos en la base de datos.
     *
     * @param all dice si se deben obtener todas las fotos
     * @param maxResults si {@code all} es falso, limita cuántas fotos se
     * obtienen
     * @param firstResult si {@code all} es falso, dice cuál es la primer foto
     * que se obtiene
     * @return una lista de fotos siguiendo las restricciones dadas por los
     * parámetros
     */
    @SuppressWarnings("checkstyle:linelength")
    private List<FotoPuesto> findFotoPuestoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FotoPuesto.class));
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
     * Busca un FotoPuesto por ID.
     *
     * @param id el ID de la foto deseada
     * @return el FotoPuesto con ID igual a {@code id} o null si no existe.
     */
    public FotoPuesto findFotoPuesto(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(FotoPuesto.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Cuenta cuántas fotos hay en la base de datos.
     *
     * @return el número de fotos existentes
     */
    public int getFotoPuestoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FotoPuesto> rt = cq.from(FotoPuesto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
