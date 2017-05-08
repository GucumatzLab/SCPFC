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
public class ControladorJpaFotoPuesto implements Serializable {

    public ControladorJpaFotoPuesto(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

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

    public List<FotoPuesto> buscarPorPuesto(Puesto puesto) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<FotoPuesto> query
                    = em.createNamedQuery("FotoPuesto.buscarPorPuesto", FotoPuesto.class);
            query.setParameter("puesto", puesto);
            List<FotoPuesto> results = query.getResultList();
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
