/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gucumatz.scpfc.web;

import gucumatz.scpfc.modelo.Usuario;
import gucumatz.scpfc.modelo.db.FabricaControladorJpa;
import gucumatz.scpfc.modelo.db.UsuarioJpaController;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

//
@ManagedBean
@ViewScoped
public class Registro implements Serializable {

    private final UsuarioJpaController jpaUsuario;

    private String nombreDeUsuario;
    private String correoElectronico;
    private String contrasena;
    private String confirmacionDeContrasena;

    private FacesContext facesContext;

    private boolean hayErrores;

    /**
     * Creates a new instance of Registro
     */
    public Registro() {
        jpaUsuario = new FabricaControladorJpa().obtenerControladorJpaUsuario();
        hayErrores = false;
    }

    public String getNombreDeUsuario() {
        return nombreDeUsuario;
    }

    public void setNombreDeUsuario(String nombreDeUsuario) {
        this.nombreDeUsuario = nombreDeUsuario;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getConfirmacionDeContrasena() {
        return confirmacionDeContrasena;
    }

    public void setConfirmacionDeContrasena(String confirmacionDeContrasena) {
        this.confirmacionDeContrasena = confirmacionDeContrasena;
    }

    public void validarDatos() {
        facesContext = FacesContext.getCurrentInstance();
        hayErrores = false;

        comprobarNombreDeUsuarioDisponible();
        comprobarCorreoElectronicoDisponible();
        comprobarCorreoElectronicoEsDeCiencias();
        comprobarContrasenasCoinciden();
    }

    public String registrar() {
        validarDatos();

        if (hayErrores) {
            return null;
        }

        Usuario u = new Usuario();
        u.setNombre(nombreDeUsuario);
        u.setCorreoElectronico(correoElectronico);
        u.setContrasena(contrasena);
        u.setConfirmada(false);
        u.setEsAdministrador(false);
        u.setCodigoDeActivacion("asdf");

        jpaUsuario.create(u);

        return null;
    }

    private void comprobarNombreDeUsuarioDisponible() {
        Usuario usuario = jpaUsuario.findByNombre(nombreDeUsuario);
        if (usuario != null) {
            agregarError("El nombre de usuario ya existe", "nombreDeUsuario");
        }
    }

    private void comprobarCorreoElectronicoDisponible() {
        Usuario usuario = jpaUsuario.findByCorreoElectronico(correoElectronico);
        if (usuario != null) {
            agregarError("El correo electrónico ya existe", "correoElectronico");
        }
    }

    private void comprobarCorreoElectronicoEsDeCiencias() {
        if (!correoElectronico.endsWith("@ciencias.unam.mx")) {
            agregarError("El correo electrónico debe ser de @ciencias.unam.mx", "correoElectronico");
        }
    }

    private void comprobarContrasenasCoinciden() {
        if (!contrasena.equals(confirmacionDeContrasena)) {
            agregarError("Las contraseñas no coinciden", "confirmacionDeContrasena");
        }
    }

    private void agregarError(String mensaje, String elemento) {
        hayErrores = true;
        FacesMessage facesMessage
                = new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, null);
        facesContext.addMessage(null, facesMessage);
    }

}
