/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gucumatz.scpfc.modelo.db;

import gucumatz.scpfc.modelo.Fotospuesto;
import gucumatz.scpfc.modelo.FotospuestoPK;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import gucumatz.scpfc.modelo.Puesto;
import gucumatz.scpfc.modelo.db.exceptions.NonexistentEntityException;
import gucumatz.scpfc.modelo.db.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.LinkedList;

/**
 *
 * @author Pablo Gerardo Gonzalez Lopez
 */
public class FotospuestoJpaController implements Serializable {

    public FotospuestoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Fotospuesto fotospuesto) throws PreexistingEntityException, Exception {
        if (fotospuesto.getFotospuestoPK() == null) {
            fotospuesto.setFotospuestoPK(new FotospuestoPK());
        }
        fotospuesto.getFotospuestoPK().setIdPuesto(fotospuesto.getPuesto().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Puesto puesto = fotospuesto.getPuesto();
            if (puesto != null) {
                puesto = em.getReference(puesto.getClass(), puesto.getId());
                fotospuesto.setPuesto(puesto);
            }
            em.persist(fotospuesto);
            if (puesto != null) {
                puesto.getFotospuestoList().add(fotospuesto);
                puesto = em.merge(puesto);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFotospuesto(fotospuesto.getFotospuestoPK()) != null) {
                throw new PreexistingEntityException("Fotospuesto " + fotospuesto + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Fotospuesto fotospuesto) throws NonexistentEntityException, Exception {
        fotospuesto.getFotospuestoPK().setIdPuesto(fotospuesto.getPuesto().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Fotospuesto persistentFotospuesto = em.find(Fotospuesto.class, fotospuesto.getFotospuestoPK());
            Puesto puestoOld = persistentFotospuesto.getPuesto();
            Puesto puestoNew = fotospuesto.getPuesto();
            if (puestoNew != null) {
                puestoNew = em.getReference(puestoNew.getClass(), puestoNew.getId());
                fotospuesto.setPuesto(puestoNew);
            }
            fotospuesto = em.merge(fotospuesto);
            if (puestoOld != null && !puestoOld.equals(puestoNew)) {
                puestoOld.getFotospuestoList().remove(fotospuesto);
                puestoOld = em.merge(puestoOld);
            }
            if (puestoNew != null && !puestoNew.equals(puestoOld)) {
                puestoNew.getFotospuestoList().add(fotospuesto);
                puestoNew = em.merge(puestoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                FotospuestoPK id = fotospuesto.getFotospuestoPK();
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

    public void destroy(FotospuestoPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Fotospuesto fotospuesto;
            try {
                fotospuesto = em.getReference(Fotospuesto.class, id);
                fotospuesto.getFotospuestoPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The fotospuesto with id " + id + " no longer exists.", enfe);
            }
            Puesto puesto = fotospuesto.getPuesto();
            if (puesto != null) {
                puesto.getFotospuestoList().remove(fotospuesto);
                puesto = em.merge(puesto);
            }
            em.remove(fotospuesto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<FotospuestoPK> findFotospuestoById(Long id){
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Fotospuesto> query
                    = em.createNamedQuery("Fotospuesto.findByIdPuesto", Fotospuesto.class);
            query.setParameter("idPuesto", id);
            List<Fotospuesto> results = query.getResultList();
            List<FotospuestoPK> resultsP = new LinkedList<>();
            for(Fotospuesto fp : results){
                resultsP.add(fp.getFotospuestoPK());
            }
            if (results.isEmpty()) {
                return null;
            }
            return resultsP;
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

    public Fotospuesto findFotospuesto(FotospuestoPK id) {
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