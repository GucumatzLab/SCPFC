/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gucumatz.scpfc.modelo.db;

import gucumatz.scpfc.modelo.Fotospuesto;
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
public class FotospuestoJpaController implements Serializable {

    public FotospuestoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Fotospuesto fotospuesto) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Puesto puestoId = fotospuesto.getPuestoId();
            if (puestoId != null) {
                puestoId = em.getReference(puestoId.getClass(), puestoId.getId());
                fotospuesto.setPuestoId(puestoId);
            }
            em.persist(fotospuesto);
            if (puestoId != null) {
                puestoId.getFotospuestoList().add(fotospuesto);
                puestoId = em.merge(puestoId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Fotospuesto fotospuesto) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Fotospuesto persistentFotospuesto = em.find(Fotospuesto.class, fotospuesto.getId());
            Puesto puestoIdOld = persistentFotospuesto.getPuestoId();
            Puesto puestoIdNew = fotospuesto.getPuestoId();
            if (puestoIdNew != null) {
                puestoIdNew = em.getReference(puestoIdNew.getClass(), puestoIdNew.getId());
                fotospuesto.setPuestoId(puestoIdNew);
            }
            fotospuesto = em.merge(fotospuesto);
            if (puestoIdOld != null && !puestoIdOld.equals(puestoIdNew)) {
                puestoIdOld.getFotospuestoList().remove(fotospuesto);
                puestoIdOld = em.merge(puestoIdOld);
            }
            if (puestoIdNew != null && !puestoIdNew.equals(puestoIdOld)) {
                puestoIdNew.getFotospuestoList().add(fotospuesto);
                puestoIdNew = em.merge(puestoIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = fotospuesto.getId();
                if (findFotospuesto(id) == null) {
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
            Fotospuesto fotospuesto;
            try {
                fotospuesto = em.getReference(Fotospuesto.class, id);
                fotospuesto.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The fotospuesto with id " + id + " no longer exists.", enfe);
            }
            Puesto puestoId = fotospuesto.getPuestoId();
            if (puestoId != null) {
                puestoId.getFotospuestoList().remove(fotospuesto);
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

    public List<Fotospuesto> findFotospuestoByPuestoId(Puesto puestoId){
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Fotospuesto> query
                    = em.createNamedQuery("Fotospuesto.findByPuestoId", Fotospuesto.class);
            query.setParameter("puestoId", puestoId);
            List<Fotospuesto> results = query.getResultList();
            if (results.isEmpty()) {
                return null;
            }
            return results;
        } finally {
            em.close();
        }
    }
    
    public List<Fotospuesto> findFotospuestoEntities() {
        return findFotospuestoEntities(true, -1, -1);
    }

    public List<Fotospuesto> findFotospuestoEntities(int maxResults, int firstResult) {
        return findFotospuestoEntities(false, maxResults, firstResult);
    }

    private List<Fotospuesto> findFotospuestoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Fotospuesto.class));
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

    public Fotospuesto findFotospuesto(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Fotospuesto.class, id);
        } finally {
            em.close();
        }
    }

    public int getFotospuestoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Fotospuesto> rt = cq.from(Fotospuesto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
