/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gucumatz.scpfc.modelo.db;

import gucumatz.scpfc.modelo.Usuario;
import gucumatz.scpfc.modelo.db.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import gucumatz.scpfc.modelo.Calificacion;
import java.util.ArrayList;
import gucumatz.scpfc.modelo.Comentario;
import gucumatz.scpfc.modelo.db.exceptions.IllegalOrphanException;

/**
 *
 * @author lchacon
 */
public class ControladorJpaUsuario implements Serializable {

    public ControladorJpaUsuario(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void crear(Usuario usuario) {
        if (usuario.getCalificaciones() == null) {
            usuario.setCalificaciones(new ArrayList<Calificacion>());
        }
        if (usuario.getComentarios() == null) {
            usuario.setComentarios(new ArrayList<Comentario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Calificacion> attachedCalificaciones = new ArrayList<Calificacion>();
            for (Calificacion calificacionesCalificacionToAttach : usuario.getCalificaciones()) {
                calificacionesCalificacionToAttach = em.getReference(calificacionesCalificacionToAttach.getClass(), calificacionesCalificacionToAttach.getId());
                attachedCalificaciones.add(calificacionesCalificacionToAttach);
            }
            usuario.setCalificaciones(attachedCalificaciones);
            List<Comentario> attachedComentarios = new ArrayList<Comentario>();
            for (Comentario comentariosComentarioToAttach : usuario.getComentarios()) {
                comentariosComentarioToAttach = em.getReference(comentariosComentarioToAttach.getClass(), comentariosComentarioToAttach.getId());
                attachedComentarios.add(comentariosComentarioToAttach);
            }
            usuario.setComentarios(attachedComentarios);
            em.persist(usuario);
            for (Calificacion calificacionesCalificacion : usuario.getCalificaciones()) {
                Usuario oldUsuarioOfCalificacionesCalificacion = calificacionesCalificacion.getUsuario();
                calificacionesCalificacion.setUsuario(usuario);
                calificacionesCalificacion = em.merge(calificacionesCalificacion);
                if (oldUsuarioOfCalificacionesCalificacion != null) {
                    oldUsuarioOfCalificacionesCalificacion.getCalificaciones().remove(calificacionesCalificacion);
                    oldUsuarioOfCalificacionesCalificacion = em.merge(oldUsuarioOfCalificacionesCalificacion);
                }
            }
            for (Comentario comentariosComentario : usuario.getComentarios()) {
                Usuario oldUsuarioOfComentariosComentario = comentariosComentario.getUsuario();
                comentariosComentario.setUsuario(usuario);
                comentariosComentario = em.merge(comentariosComentario);
                if (oldUsuarioOfComentariosComentario != null) {
                    oldUsuarioOfComentariosComentario.getComentarios().remove(comentariosComentario);
                    oldUsuarioOfComentariosComentario = em.merge(oldUsuarioOfComentariosComentario);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void editar(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getId());
            List<Calificacion> calificacionesOld = persistentUsuario.getCalificaciones();
            List<Calificacion> calificacionesNew = usuario.getCalificaciones();
            List<Comentario> comentariosOld = persistentUsuario.getComentarios();
            List<Comentario> comentariosNew = usuario.getComentarios();
            List<String> illegalOrphanMessages = null;
            for (Calificacion calificacionesOldCalificacion : calificacionesOld) {
                if (!calificacionesNew.contains(calificacionesOldCalificacion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Calificacion " + calificacionesOldCalificacion + " since its usuario field is not nullable.");
                }
            }
            for (Comentario comentariosOldComentario : comentariosOld) {
                if (!comentariosNew.contains(comentariosOldComentario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Comentario " + comentariosOldComentario + " since its usuario field is not nullable.");
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
            usuario.setCalificaciones(calificacionesNew);
            List<Comentario> attachedComentariosNew = new ArrayList<Comentario>();
            for (Comentario comentariosNewComentarioToAttach : comentariosNew) {
                comentariosNewComentarioToAttach = em.getReference(comentariosNewComentarioToAttach.getClass(), comentariosNewComentarioToAttach.getId());
                attachedComentariosNew.add(comentariosNewComentarioToAttach);
            }
            comentariosNew = attachedComentariosNew;
            usuario.setComentarios(comentariosNew);
            usuario = em.merge(usuario);
            for (Calificacion calificacionesNewCalificacion : calificacionesNew) {
                if (!calificacionesOld.contains(calificacionesNewCalificacion)) {
                    Usuario oldUsuarioOfCalificacionesNewCalificacion = calificacionesNewCalificacion.getUsuario();
                    calificacionesNewCalificacion.setUsuario(usuario);
                    calificacionesNewCalificacion = em.merge(calificacionesNewCalificacion);
                    if (oldUsuarioOfCalificacionesNewCalificacion != null && !oldUsuarioOfCalificacionesNewCalificacion.equals(usuario)) {
                        oldUsuarioOfCalificacionesNewCalificacion.getCalificaciones().remove(calificacionesNewCalificacion);
                        oldUsuarioOfCalificacionesNewCalificacion = em.merge(oldUsuarioOfCalificacionesNewCalificacion);
                    }
                }
            }
            for (Comentario comentariosNewComentario : comentariosNew) {
                if (!comentariosOld.contains(comentariosNewComentario)) {
                    Usuario oldUsuarioOfComentariosNewComentario = comentariosNewComentario.getUsuario();
                    comentariosNewComentario.setUsuario(usuario);
                    comentariosNewComentario = em.merge(comentariosNewComentario);
                    if (oldUsuarioOfComentariosNewComentario != null && !oldUsuarioOfComentariosNewComentario.equals(usuario)) {
                        oldUsuarioOfComentariosNewComentario.getComentarios().remove(comentariosNewComentario);
                        oldUsuarioOfComentariosNewComentario = em.merge(oldUsuarioOfComentariosNewComentario);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = usuario.getId();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Calificacion> calificacionesOrphanCheck = usuario.getCalificaciones();
            for (Calificacion calificacionesOrphanCheckCalificacion : calificacionesOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Calificacion " + calificacionesOrphanCheckCalificacion + " in its calificaciones field has a non-nullable usuario field.");
            }
            List<Comentario> comentariosOrphanCheck = usuario.getComentarios();
            for (Comentario comentariosOrphanCheckComentario : comentariosOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Comentario " + comentariosOrphanCheckComentario + " in its comentarios field has a non-nullable usuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Usuario findByNombre(String nombre) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Usuario> query
                    = em.createNamedQuery("Usuario.findByNombre", Usuario.class);
            query.setParameter("nombre", nombre);
            List<Usuario> results = query.getResultList();
            if (results.isEmpty()) {
                return null;
            }
            return results.get(0);
        } finally {
            em.close();
        }
    }

    public Usuario findByCorreoElectronico(String correoElectronico) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Usuario> query
                    = em.createNamedQuery("Usuario.findByCorreoElectronico", Usuario.class);
            query.setParameter("correoElectronico", correoElectronico);
            List<Usuario> results = query.getResultList();
            if (results.isEmpty()) {
                return null;
            }
            return results.get(0);
        } finally {
            em.close();
        }
    }

    /**
     * Busca un usuario por nombre o correo electrónico.
     *
     * @param cuenta nombre de usuario o correo electrónico a buscar.
     * @return el usuario cuyo nombre o correo electrónico es `cuenta`, o null
     * si no existe.
     */
    public Usuario buscarUsuario(String cuenta) {
        Usuario u = findByNombre(cuenta);
        if (u != null) {
            return u;
        }

        return findByCorreoElectronico(cuenta);
    }

}
