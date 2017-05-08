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
import gucumatz.scpfc.modelo.FotoPuesto;
import gucumatz.scpfc.modelo.Comentario;
import gucumatz.scpfc.modelo.Puesto;
import gucumatz.scpfc.modelo.db.exceptions.IllegalOrphanException;
import gucumatz.scpfc.modelo.db.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * @author lchacon
 */
public class ControladorJpaPuesto implements Serializable {

    public ControladorJpaPuesto(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void crear(Puesto puesto) {
        if (puesto.getCalificaciones() == null) {
            puesto.setCalificaciones(new ArrayList<Calificacion>());
        }
        if (puesto.getFotosPuesto() == null) {
            puesto.setFotosPuesto(new ArrayList<FotoPuesto>());
        }
        if (puesto.getComentarios() == null) {
            puesto.setComentarios(new ArrayList<Comentario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Calificacion> attachedCalificaciones = new ArrayList<Calificacion>();
            for (Calificacion calificacionesCalificacionToAttach : puesto.getCalificaciones()) {
                calificacionesCalificacionToAttach = em.getReference(calificacionesCalificacionToAttach.getClass(), calificacionesCalificacionToAttach.getId());
                attachedCalificaciones.add(calificacionesCalificacionToAttach);
            }
            puesto.setCalificaciones(attachedCalificaciones);
            List<FotoPuesto> attachedFotosPuesto = new ArrayList<FotoPuesto>();
            for (FotoPuesto fotosPuestoFotoPuestoToAttach : puesto.getFotosPuesto()) {
                fotosPuestoFotoPuestoToAttach = em.getReference(fotosPuestoFotoPuestoToAttach.getClass(), fotosPuestoFotoPuestoToAttach.getId());
                attachedFotosPuesto.add(fotosPuestoFotoPuestoToAttach);
            }
            puesto.setFotosPuesto(attachedFotosPuesto);
            List<Comentario> attachedComentarios = new ArrayList<Comentario>();
            for (Comentario comentariosComentarioToAttach : puesto.getComentarios()) {
                comentariosComentarioToAttach = em.getReference(comentariosComentarioToAttach.getClass(), comentariosComentarioToAttach.getId());
                attachedComentarios.add(comentariosComentarioToAttach);
            }
            puesto.setComentarios(attachedComentarios);
            em.persist(puesto);
            for (Calificacion calificacionesCalificacion : puesto.getCalificaciones()) {
                Puesto oldPuestoOfCalificacionesCalificacion = calificacionesCalificacion.getPuesto();
                calificacionesCalificacion.setPuesto(puesto);
                calificacionesCalificacion = em.merge(calificacionesCalificacion);
                if (oldPuestoOfCalificacionesCalificacion != null) {
                    oldPuestoOfCalificacionesCalificacion.getCalificaciones().remove(calificacionesCalificacion);
                    oldPuestoOfCalificacionesCalificacion = em.merge(oldPuestoOfCalificacionesCalificacion);
                }
            }
            for (FotoPuesto fotosPuestoFotoPuesto : puesto.getFotosPuesto()) {
                Puesto oldPuestoOfFotosPuestoFotoPuesto = fotosPuestoFotoPuesto.getPuesto();
                fotosPuestoFotoPuesto.setPuesto(puesto);
                fotosPuestoFotoPuesto = em.merge(fotosPuestoFotoPuesto);
                if (oldPuestoOfFotosPuestoFotoPuesto != null) {
                    oldPuestoOfFotosPuestoFotoPuesto.getFotosPuesto().remove(fotosPuestoFotoPuesto);
                    oldPuestoOfFotosPuestoFotoPuesto = em.merge(oldPuestoOfFotosPuestoFotoPuesto);
                }
            }
            for (Comentario comentariosComentario : puesto.getComentarios()) {
                Puesto oldPuestoOfComentariosComentario = comentariosComentario.getPuesto();
                comentariosComentario.setPuesto(puesto);
                comentariosComentario = em.merge(comentariosComentario);
                if (oldPuestoOfComentariosComentario != null) {
                    oldPuestoOfComentariosComentario.getComentarios().remove(comentariosComentario);
                    oldPuestoOfComentariosComentario = em.merge(oldPuestoOfComentariosComentario);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void editar(Puesto puesto) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Puesto persistentPuesto = em.find(Puesto.class, puesto.getId());
            List<Calificacion> calificacionesOld = persistentPuesto.getCalificaciones();
            List<Calificacion> calificacionesNew = puesto.getCalificaciones();
            List<FotoPuesto> fotosPuestoOld = persistentPuesto.getFotosPuesto();
            List<FotoPuesto> fotosPuestoNew = puesto.getFotosPuesto();
            List<Comentario> comentariosOld = persistentPuesto.getComentarios();
            List<Comentario> comentariosNew = puesto.getComentarios();
            List<String> illegalOrphanMessages = null;
            for (Calificacion calificacionesOldCalificacion : calificacionesOld) {
                if (!calificacionesNew.contains(calificacionesOldCalificacion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Calificacion " + calificacionesOldCalificacion + " since its puesto field is not nullable.");
                }
            }
            for (FotoPuesto fotosPuestoOldFotoPuesto : fotosPuestoOld) {
                if (!fotosPuestoNew.contains(fotosPuestoOldFotoPuesto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain FotoPuesto " + fotosPuestoOldFotoPuesto + " since its puesto field is not nullable.");
                }
            }
            for (Comentario comentariosOldComentario : comentariosOld) {
                if (!comentariosNew.contains(comentariosOldComentario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Comentario " + comentariosOldComentario + " since its puesto field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Calificacion> attachedCalificacionesNew = new ArrayList<Calificacion>();
            for (Calificacion calificacionesNewCalificacionToAttach : calificacionesNew) {
                calificacionesNewCalificacionToAttach = em.getReference(calificacionesNewCalificacionToAttach.getClass(), calificacionesNewCalificacionToAttach.getId());
                attachedCalificacionesNew.add(calificacionesNewCalificacionToAttach);
            }
            calificacionesNew = attachedCalificacionesNew;
            puesto.setCalificaciones(calificacionesNew);
            List<FotoPuesto> attachedFotosPuestoNew = new ArrayList<FotoPuesto>();
            for (FotoPuesto fotosPuestoNewFotoPuestoToAttach : fotosPuestoNew) {
                fotosPuestoNewFotoPuestoToAttach = em.getReference(fotosPuestoNewFotoPuestoToAttach.getClass(), fotosPuestoNewFotoPuestoToAttach.getId());
                attachedFotosPuestoNew.add(fotosPuestoNewFotoPuestoToAttach);
            }
            fotosPuestoNew = attachedFotosPuestoNew;
            puesto.setFotosPuesto(fotosPuestoNew);
            List<Comentario> attachedComentariosNew = new ArrayList<Comentario>();
            for (Comentario comentariosNewComentarioToAttach : comentariosNew) {
                comentariosNewComentarioToAttach = em.getReference(comentariosNewComentarioToAttach.getClass(), comentariosNewComentarioToAttach.getId());
                attachedComentariosNew.add(comentariosNewComentarioToAttach);
            }
            comentariosNew = attachedComentariosNew;
            puesto.setComentarios(comentariosNew);
            puesto = em.merge(puesto);
            for (Calificacion calificacionesNewCalificacion : calificacionesNew) {
                if (!calificacionesOld.contains(calificacionesNewCalificacion)) {
                    Puesto oldPuestoOfCalificacionesNewCalificacion = calificacionesNewCalificacion.getPuesto();
                    calificacionesNewCalificacion.setPuesto(puesto);
                    calificacionesNewCalificacion = em.merge(calificacionesNewCalificacion);
                    if (oldPuestoOfCalificacionesNewCalificacion != null && !oldPuestoOfCalificacionesNewCalificacion.equals(puesto)) {
                        oldPuestoOfCalificacionesNewCalificacion.getCalificaciones().remove(calificacionesNewCalificacion);
                        oldPuestoOfCalificacionesNewCalificacion = em.merge(oldPuestoOfCalificacionesNewCalificacion);
                    }
                }
            }
            for (FotoPuesto fotosPuestoNewFotoPuesto : fotosPuestoNew) {
                if (!fotosPuestoOld.contains(fotosPuestoNewFotoPuesto)) {
                    Puesto oldPuestoOfFotosPuestoNewFotoPuesto = fotosPuestoNewFotoPuesto.getPuesto();
                    fotosPuestoNewFotoPuesto.setPuesto(puesto);
                    fotosPuestoNewFotoPuesto = em.merge(fotosPuestoNewFotoPuesto);
                    if (oldPuestoOfFotosPuestoNewFotoPuesto != null && !oldPuestoOfFotosPuestoNewFotoPuesto.equals(puesto)) {
                        oldPuestoOfFotosPuestoNewFotoPuesto.getFotosPuesto().remove(fotosPuestoNewFotoPuesto);
                        oldPuestoOfFotosPuestoNewFotoPuesto = em.merge(oldPuestoOfFotosPuestoNewFotoPuesto);
                    }
                }
            }
            for (Comentario comentariosNewComentario : comentariosNew) {
                if (!comentariosOld.contains(comentariosNewComentario)) {
                    Puesto oldPuestoOfComentariosNewComentario = comentariosNewComentario.getPuesto();
                    comentariosNewComentario.setPuesto(puesto);
                    comentariosNewComentario = em.merge(comentariosNewComentario);
                    if (oldPuestoOfComentariosNewComentario != null && !oldPuestoOfComentariosNewComentario.equals(puesto)) {
                        oldPuestoOfComentariosNewComentario.getComentarios().remove(comentariosNewComentario);
                        oldPuestoOfComentariosNewComentario = em.merge(oldPuestoOfComentariosNewComentario);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = puesto.getId();
                if (buscarPorId(id) == null) {
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

    public void destruir(Long id) throws IllegalOrphanException, NonexistentEntityException {
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
            List<Calificacion> calificacionesOrphanCheck = puesto.getCalificaciones();
            for (Calificacion calificacionesOrphanCheckCalificacion : calificacionesOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Puesto (" + puesto + ") cannot be destroyed since the Calificacion " + calificacionesOrphanCheckCalificacion + " in its calificaciones field has a non-nullable puesto field.");
            }
            List<FotoPuesto> fotosPuestoOrphanCheck = puesto.getFotosPuesto();
            for (FotoPuesto fotosPuestoOrphanCheckFotoPuesto : fotosPuestoOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Puesto (" + puesto + ") cannot be destroyed since the FotoPuesto " + fotosPuestoOrphanCheckFotoPuesto + " in its fotosPuesto field has a non-nullable puesto field.");
            }
            List<Comentario> comentariosOrphanCheck = puesto.getComentarios();
            for (Comentario comentariosOrphanCheckComentario : comentariosOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Puesto (" + puesto + ") cannot be destroyed since the Comentario " + comentariosOrphanCheckComentario + " in its comentarios field has a non-nullable puesto field.");
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

    public List<Puesto> buscarTodos() {
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

    public Puesto buscarPorId(Long id) {
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

    public Puesto buscarPorNombre(String nombre) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Puesto> query = em.createNamedQuery("Puesto.buscarPorNombre", Puesto.class);
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
