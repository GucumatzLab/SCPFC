/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gucumatz.scpfc.modelo.db;

import gucumatz.scpfc.modelo.FotoPuesto;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import gucumatz.scpfc.modelo.Puesto;
import gucumatz.scpfc.modelo.db.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * @author lchacon
 */
public class FotoPuestoJpaController implements Serializable {

    public FotoPuestoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(FotoPuesto fotospuesto) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Puesto puestoId = fotospuesto.getPuesto();
            if (puestoId != null) {
                puestoId = em.getReference(puestoId.getClass(), puestoId.getId());
                fotospuesto.setPuesto(puestoId);
            }
            em.persist(fotospuesto);
            if (puestoId != null) {
                puestoId.getFotosPuesto().add(fotospuesto);
                puestoId = em.merge(puestoId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(FotoPuesto fotospuesto) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FotoPuesto persistentFotoPuesto = em.find(FotoPuesto.class, fotospuesto.getId());
            Puesto puestoIdOld = persistentFotoPuesto.getPuesto();
            Puesto puestoIdNew = fotospuesto.getPuesto();
            if (puestoIdNew != null) {
                puestoIdNew = em.getReference(puestoIdNew.getClass(), puestoIdNew.getId());
                fotospuesto.setPuesto(puestoIdNew);
            }
            fotospuesto = em.merge(fotospuesto);
            if (puestoIdOld != null && !puestoIdOld.equals(puestoIdNew)) {
                puestoIdOld.getFotosPuesto().remove(fotospuesto);
                puestoIdOld = em.merge(puestoIdOld);
            }
            if (puestoIdNew != null && !puestoIdNew.equals(puestoIdOld)) {
                puestoIdNew.getFotosPuesto().add(fotospuesto);
                puestoIdNew = em.merge(puestoIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = fotospuesto.getId();
                if (findFotoPuesto(id) == null) {
                    throw new NonexistentEntityException("The fotospuesto with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FotoPuesto fotospuesto;
            try {
                fotospuesto = em.getReference(FotoPuesto.class, id);
                fotospuesto.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The fotospuesto with id " + id + " no longer exists.", enfe);
            }
            Puesto puestoId = fotospuesto.getPuesto();
            if (puestoId != null) {
                puestoId.getFotosPuesto().remove(fotospuesto);
                puestoId = em.merge(puestoId);
            }
            em.remove(fotospuesto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<FotoPuesto> findFotoPuestoByPuestoId(Puesto puestoId){
        EntityManager em = getEntityManager();
        try {
            TypedQuery<FotoPuesto> query
                    = em.createNamedQuery("FotoPuesto.findByPuestoId", FotoPuesto.class);
            query.setParameter("puestoId", puestoId);
            List<FotoPuesto> results = query.getResultList();
            if (results.isEmpty()) {
                return null;
            }
            return results;
        } finally {
            em.close();
        }
    }
    
    public List<FotoPuesto> findFotoPuestoEntities() {
        return findFotoPuestoEntities(true, -1, -1);
    }

    public List<FotoPuesto> findFotoPuestoEntities(int maxResults, int firstResult) {
        return findFotoPuestoEntities(false, maxResults, firstResult);
    }

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

    public FotoPuesto findFotoPuesto(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(FotoPuesto.class, id);
        } finally {
            em.close();
        }
    }

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
