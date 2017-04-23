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
import gucumatz.scpfc.modelo.Calificacion;
import java.util.ArrayList;
import java.util.Collection;
import gucumatz.scpfc.modelo.Comentario;
import gucumatz.scpfc.modelo.Puesto;
import gucumatz.scpfc.modelo.db.exceptions.IllegalOrphanException;
import gucumatz.scpfc.modelo.db.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * @author lchacon
 */
public class PuestoJpaController implements Serializable {

    public PuestoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Puesto puesto) {
        if (puesto.getCalificacionCollection() == null) {
            puesto.setCalificacionCollection(new ArrayList<Calificacion>());
        }
        if (puesto.getComentarioCollection() == null) {
            puesto.setComentarioCollection(new ArrayList<Comentario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Calificacion> attachedCalificacionCollection = new ArrayList<Calificacion>();
            for (Calificacion calificacionCollectionCalificacionToAttach : puesto.getCalificacionCollection()) {
                calificacionCollectionCalificacionToAttach = em.getReference(calificacionCollectionCalificacionToAttach.getClass(), calificacionCollectionCalificacionToAttach.getId());
                attachedCalificacionCollection.add(calificacionCollectionCalificacionToAttach);
            }
            puesto.setCalificacionCollection(attachedCalificacionCollection);
            Collection<Comentario> attachedComentarioCollection = new ArrayList<Comentario>();
            for (Comentario comentarioCollectionComentarioToAttach : puesto.getComentarioCollection()) {
                comentarioCollectionComentarioToAttach = em.getReference(comentarioCollectionComentarioToAttach.getClass(), comentarioCollectionComentarioToAttach.getId());
                attachedComentarioCollection.add(comentarioCollectionComentarioToAttach);
            }
            puesto.setComentarioCollection(attachedComentarioCollection);
            em.persist(puesto);
            for (Calificacion calificacionCollectionCalificacion : puesto.getCalificacionCollection()) {
                Puesto oldPuestoIdOfCalificacionCollectionCalificacion = calificacionCollectionCalificacion.getPuestoId();
                calificacionCollectionCalificacion.setPuestoId(puesto);
                calificacionCollectionCalificacion = em.merge(calificacionCollectionCalificacion);
                if (oldPuestoIdOfCalificacionCollectionCalificacion != null) {
                    oldPuestoIdOfCalificacionCollectionCalificacion.getCalificacionCollection().remove(calificacionCollectionCalificacion);
                    oldPuestoIdOfCalificacionCollectionCalificacion = em.merge(oldPuestoIdOfCalificacionCollectionCalificacion);
                }
            }
            for (Comentario comentarioCollectionComentario : puesto.getComentarioCollection()) {
                Puesto oldPuestoIdOfComentarioCollectionComentario = comentarioCollectionComentario.getPuestoId();
                comentarioCollectionComentario.setPuestoId(puesto);
                comentarioCollectionComentario = em.merge(comentarioCollectionComentario);
                if (oldPuestoIdOfComentarioCollectionComentario != null) {
                    oldPuestoIdOfComentarioCollectionComentario.getComentarioCollection().remove(comentarioCollectionComentario);
                    oldPuestoIdOfComentarioCollectionComentario = em.merge(oldPuestoIdOfComentarioCollectionComentario);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Puesto puesto) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Puesto persistentPuesto = em.find(Puesto.class, puesto.getId());
            Collection<Calificacion> calificacionCollectionOld = persistentPuesto.getCalificacionCollection();
            Collection<Calificacion> calificacionCollectionNew = puesto.getCalificacionCollection();
            Collection<Comentario> comentarioCollectionOld = persistentPuesto.getComentarioCollection();
            Collection<Comentario> comentarioCollectionNew = puesto.getComentarioCollection();
            List<String> illegalOrphanMessages = null;
            for (Calificacion calificacionCollectionOldCalificacion : calificacionCollectionOld) {
                if (!calificacionCollectionNew.contains(calificacionCollectionOldCalificacion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Calificacion " + calificacionCollectionOldCalificacion + " since its puestoId field is not nullable.");
                }
            }
            for (Comentario comentarioCollectionOldComentario : comentarioCollectionOld) {
                if (!comentarioCollectionNew.contains(comentarioCollectionOldComentario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Comentario " + comentarioCollectionOldComentario + " since its puestoId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Calificacion> attachedCalificacionCollectionNew = new ArrayList<Calificacion>();
            for (Calificacion calificacionCollectionNewCalificacionToAttach : calificacionCollectionNew) {
                calificacionCollectionNewCalificacionToAttach = em.getReference(calificacionCollectionNewCalificacionToAttach.getClass(), calificacionCollectionNewCalificacionToAttach.getId());
                attachedCalificacionCollectionNew.add(calificacionCollectionNewCalificacionToAttach);
            }
            calificacionCollectionNew = attachedCalificacionCollectionNew;
            puesto.setCalificacionCollection(calificacionCollectionNew);
            Collection<Comentario> attachedComentarioCollectionNew = new ArrayList<Comentario>();
            for (Comentario comentarioCollectionNewComentarioToAttach : comentarioCollectionNew) {
                comentarioCollectionNewComentarioToAttach = em.getReference(comentarioCollectionNewComentarioToAttach.getClass(), comentarioCollectionNewComentarioToAttach.getId());
                attachedComentarioCollectionNew.add(comentarioCollectionNewComentarioToAttach);
            }
            comentarioCollectionNew = attachedComentarioCollectionNew;
            puesto.setComentarioCollection(comentarioCollectionNew);
            puesto = em.merge(puesto);
            for (Calificacion calificacionCollectionNewCalificacion : calificacionCollectionNew) {
                if (!calificacionCollectionOld.contains(calificacionCollectionNewCalificacion)) {
                    Puesto oldPuestoIdOfCalificacionCollectionNewCalificacion = calificacionCollectionNewCalificacion.getPuestoId();
                    calificacionCollectionNewCalificacion.setPuestoId(puesto);
                    calificacionCollectionNewCalificacion = em.merge(calificacionCollectionNewCalificacion);
                    if (oldPuestoIdOfCalificacionCollectionNewCalificacion != null && !oldPuestoIdOfCalificacionCollectionNewCalificacion.equals(puesto)) {
                        oldPuestoIdOfCalificacionCollectionNewCalificacion.getCalificacionCollection().remove(calificacionCollectionNewCalificacion);
                        oldPuestoIdOfCalificacionCollectionNewCalificacion = em.merge(oldPuestoIdOfCalificacionCollectionNewCalificacion);
                    }
                }
            }
            for (Comentario comentarioCollectionNewComentario : comentarioCollectionNew) {
                if (!comentarioCollectionOld.contains(comentarioCollectionNewComentario)) {
                    Puesto oldPuestoIdOfComentarioCollectionNewComentario = comentarioCollectionNewComentario.getPuestoId();
                    comentarioCollectionNewComentario.setPuestoId(puesto);
                    comentarioCollectionNewComentario = em.merge(comentarioCollectionNewComentario);
                    if (oldPuestoIdOfComentarioCollectionNewComentario != null && !oldPuestoIdOfComentarioCollectionNewComentario.equals(puesto)) {
                        oldPuestoIdOfComentarioCollectionNewComentario.getComentarioCollection().remove(comentarioCollectionNewComentario);
                        oldPuestoIdOfComentarioCollectionNewComentario = em.merge(oldPuestoIdOfComentarioCollectionNewComentario);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = puesto.getId();
                if (findPuesto(id) == null) {
                    throw new NonexistentEntityException("The puesto with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Puesto puesto;
            try {
                puesto = em.getReference(Puesto.class, id);
                puesto.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The puesto with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Calificacion> calificacionCollectionOrphanCheck = puesto.getCalificacionCollection();
            for (Calificacion calificacionCollectionOrphanCheckCalificacion : calificacionCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Puesto (" + puesto + ") cannot be destroyed since the Calificacion " + calificacionCollectionOrphanCheckCalificacion + " in its calificacionCollection field has a non-nullable puestoId field.");
            }
            Collection<Comentario> comentarioCollectionOrphanCheck = puesto.getComentarioCollection();
            for (Comentario comentarioCollectionOrphanCheckComentario : comentarioCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Puesto (" + puesto + ") cannot be destroyed since the Comentario " + comentarioCollectionOrphanCheckComentario + " in its comentarioCollection field has a non-nullable puestoId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(puesto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Puesto> findPuestoEntities() {
        return findPuestoEntities(true, -1, -1);
    }

    public List<Puesto> findPuestoEntities(int maxResults, int firstResult) {
        return findPuestoEntities(false, maxResults, firstResult);
    }

    private List<Puesto> findPuestoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Puesto.class));
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

    public Puesto findPuesto(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Puesto.class, id);
        } finally {
            em.close();
        }
    }

    public int getPuestoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Puesto> rt = cq.from(Puesto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Puesto findByNombre(String nombre) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Puesto> query = em.createNamedQuery("Puesto.findByNombre", Puesto.class);
            query.setParameter("nombre", nombre);
            List<Puesto> results = query.getResultList();
            if (results.isEmpty()) {
                return null;
            }
            return results.get(0);
        } finally {
            em.close();
        }
    }
    
}
