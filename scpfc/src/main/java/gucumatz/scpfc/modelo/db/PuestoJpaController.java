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
import java.util.List;
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
        if (puesto.getCalificacionList() == null) {
            puesto.setCalificacionList(new ArrayList<Calificacion>());
        }
        if (puesto.getComentarioList() == null) {
            puesto.setComentarioList(new ArrayList<Comentario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Calificacion> attachedCalificacionList = new ArrayList<Calificacion>();
            for (Calificacion calificacionListCalificacionToAttach : puesto.getCalificacionList()) {
                calificacionListCalificacionToAttach = em.getReference(calificacionListCalificacionToAttach.getClass(), calificacionListCalificacionToAttach.getId());
                attachedCalificacionList.add(calificacionListCalificacionToAttach);
            }
            puesto.setCalificacionList(attachedCalificacionList);
            List<Comentario> attachedComentarioList = new ArrayList<Comentario>();
            for (Comentario comentarioListComentarioToAttach : puesto.getComentarioList()) {
                comentarioListComentarioToAttach = em.getReference(comentarioListComentarioToAttach.getClass(), comentarioListComentarioToAttach.getId());
                attachedComentarioList.add(comentarioListComentarioToAttach);
            }
            puesto.setComentarioList(attachedComentarioList);
            em.persist(puesto);
            for (Calificacion calificacionListCalificacion : puesto.getCalificacionList()) {
                Puesto oldPuestoIdOfCalificacionListCalificacion = calificacionListCalificacion.getPuesto();
                calificacionListCalificacion.setPuesto(puesto);
                calificacionListCalificacion = em.merge(calificacionListCalificacion);
                if (oldPuestoIdOfCalificacionListCalificacion != null) {
                    oldPuestoIdOfCalificacionListCalificacion.getCalificacionList().remove(calificacionListCalificacion);
                    oldPuestoIdOfCalificacionListCalificacion = em.merge(oldPuestoIdOfCalificacionListCalificacion);
                }
            }
            for (Comentario comentarioListComentario : puesto.getComentarioList()) {
                Puesto oldPuestoIdOfComentarioListComentario = comentarioListComentario.getPuesto();
                comentarioListComentario.setPuesto(puesto);
                comentarioListComentario = em.merge(comentarioListComentario);
                if (oldPuestoIdOfComentarioListComentario != null) {
                    oldPuestoIdOfComentarioListComentario.getComentarioList().remove(comentarioListComentario);
                    oldPuestoIdOfComentarioListComentario = em.merge(oldPuestoIdOfComentarioListComentario);
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
            List<Calificacion> calificacionListOld = persistentPuesto.getCalificacionList();
            List<Calificacion> calificacionListNew = puesto.getCalificacionList();
            List<Comentario> comentarioListOld = persistentPuesto.getComentarioList();
            List<Comentario> comentarioListNew = puesto.getComentarioList();
            List<String> illegalOrphanMessages = null;
            for (Calificacion calificacionListOldCalificacion : calificacionListOld) {
                if (!calificacionListNew.contains(calificacionListOldCalificacion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Calificacion " + calificacionListOldCalificacion + " since its puestoId field is not nullable.");
                }
            }
            for (Comentario comentarioListOldComentario : comentarioListOld) {
                if (!comentarioListNew.contains(comentarioListOldComentario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Comentario " + comentarioListOldComentario + " since its puestoId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Calificacion> attachedCalificacionListNew = new ArrayList<Calificacion>();
            for (Calificacion calificacionListNewCalificacionToAttach : calificacionListNew) {
                calificacionListNewCalificacionToAttach = em.getReference(calificacionListNewCalificacionToAttach.getClass(), calificacionListNewCalificacionToAttach.getId());
                attachedCalificacionListNew.add(calificacionListNewCalificacionToAttach);
            }
            calificacionListNew = attachedCalificacionListNew;
            puesto.setCalificacionList(calificacionListNew);
            List<Comentario> attachedComentarioListNew = new ArrayList<Comentario>();
            for (Comentario comentarioListNewComentarioToAttach : comentarioListNew) {
                comentarioListNewComentarioToAttach = em.getReference(comentarioListNewComentarioToAttach.getClass(), comentarioListNewComentarioToAttach.getId());
                attachedComentarioListNew.add(comentarioListNewComentarioToAttach);
            }
            comentarioListNew = attachedComentarioListNew;
            puesto.setComentarioList(comentarioListNew);
            puesto = em.merge(puesto);
            for (Calificacion calificacionListNewCalificacion : calificacionListNew) {
                if (!calificacionListOld.contains(calificacionListNewCalificacion)) {
                    Puesto oldPuestoIdOfCalificacionListNewCalificacion = calificacionListNewCalificacion.getPuesto();
                    calificacionListNewCalificacion.setPuesto(puesto);
                    calificacionListNewCalificacion = em.merge(calificacionListNewCalificacion);
                    if (oldPuestoIdOfCalificacionListNewCalificacion != null && !oldPuestoIdOfCalificacionListNewCalificacion.equals(puesto)) {
                        oldPuestoIdOfCalificacionListNewCalificacion.getCalificacionList().remove(calificacionListNewCalificacion);
                        oldPuestoIdOfCalificacionListNewCalificacion = em.merge(oldPuestoIdOfCalificacionListNewCalificacion);
                    }
                }
            }
            for (Comentario comentarioListNewComentario : comentarioListNew) {
                if (!comentarioListOld.contains(comentarioListNewComentario)) {
                    Puesto oldPuestoIdOfComentarioListNewComentario = comentarioListNewComentario.getPuesto();
                    comentarioListNewComentario.setPuesto(puesto);
                    comentarioListNewComentario = em.merge(comentarioListNewComentario);
                    if (oldPuestoIdOfComentarioListNewComentario != null && !oldPuestoIdOfComentarioListNewComentario.equals(puesto)) {
                        oldPuestoIdOfComentarioListNewComentario.getComentarioList().remove(comentarioListNewComentario);
                        oldPuestoIdOfComentarioListNewComentario = em.merge(oldPuestoIdOfComentarioListNewComentario);
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
            List<Calificacion> calificacionListOrphanCheck = puesto.getCalificacionList();
            for (Calificacion calificacionListOrphanCheckCalificacion : calificacionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Puesto (" + puesto + ") cannot be destroyed since the Calificacion " + calificacionListOrphanCheckCalificacion + " in its calificacionList field has a non-nullable puestoId field.");
            }
            List<Comentario> comentarioListOrphanCheck = puesto.getComentarioList();
            for (Comentario comentarioListOrphanCheckComentario : comentarioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Puesto (" + puesto + ") cannot be destroyed since the Comentario " + comentarioListOrphanCheckComentario + " in its comentarioList field has a non-nullable puestoId field.");
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
