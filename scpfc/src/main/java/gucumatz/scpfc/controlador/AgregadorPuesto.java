package gucumatz.scpfc.controlador;

import gucumatz.scpfc.modelo.db.*;
import gucumatz.scpfc.modelo.Puesto;
import gucumatz.scpfc.modelo.*;
import java.util.Locale;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.UploadedFile;
import gucumatz.scpfc.web.ManejadorDeImagenes;

/**
 * Clase para controlar un objeto de la Clase Puesto
 *
 * @author Rivera Lopez Jorge Erick
 * @version 0.1
 */
@ManagedBean
@ViewScoped
public class AgregadorPuesto implements java.io.Serializable {

    private final PuestoJpaController jpaPuesto;
    private UploadedFile foto1;
    private UploadedFile foto2;
    private UploadedFile foto3;
    private Puesto puesto = new Puesto();

    public AgregadorPuesto() {
        FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale("es-Mx"));
        jpaPuesto = new FabricaControladorJpa().obtenerControladorJpaPuesto();
    }

    public Puesto getPuesto() {
        return puesto;
    }

    /**
     * Metodo para cambiar el valor de Puesto
     *
     * @param puesto con el que se reemplazará
     */
    public void setPuesto(Puesto puesto) {
        this.puesto = puesto;
    }

    public UploadedFile getFoto1() {
        return this.foto1;
    }

    public void setFoto1(UploadedFile nuevo) {
        this.foto1 = nuevo;
    }

    public UploadedFile getFoto2() {
        return this.foto2;
    }

    public void setFoto2(UploadedFile nuevo) {
        this.foto2 = nuevo;
    }

    public UploadedFile getFoto3() {
        return this.foto3;
    }

    public void setFoto3(UploadedFile nuevo) {
        this.foto3 = nuevo;
    }

    /**
     * Metodo con el que se hace la peticion al controlador para agregar un
     * Puesto a la base de datos
     */
    public void agrega() {
        try {
            if (validaNombre(this.puesto.getNombre())) {
                if (this.puesto.getReferencias().length() == 0) {
                    this.puesto.setReferencias("Ninguna Referencia");
                }
                if (this.puesto.getLatitud() == 0 && this.puesto.getLongitud() == 0) {
                    FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Selecciona la Localizacion", null);
                    FacesContext.getCurrentInstance().addMessage(null, facesMessage);
                    return;
                }
                jpaPuesto.create(this.puesto);
                if (!(guardaImagen(this.foto1) && guardaImagen(this.foto2) && guardaImagen(this.foto3))) {
                    return;
                }
                FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Puesto Agregado con exito", null);
                FacesContext.getCurrentInstance().addMessage(null, facesMessage);
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                redirecciona();
            }
        } catch (Exception e) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR GRAVE\n" + e.getMessage(), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }

    }

    public boolean validaNombre(String nombrePuesto) {
        Puesto p = jpaPuesto.findByNombre(nombrePuesto);
        if (p != null) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR\nEl nombre ya existe", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return false;
        }
        return true;
    }

    public void redirecciona() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("./Administrar.xhtml");
        } catch (Exception e) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
    }

    public boolean guardaImagen(UploadedFile up) {
        try {
            Puesto p = jpaPuesto.findByNombre(this.puesto.getNombre());

            if (up != null) {
                String extensionFoto = null;
                String nombreDeArchivo2 = up.getFileName();
                if (up.getSize() > (4 * 1024 * 1024)) {
                    FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Tamaño invalido : Foto 1", null);
                    FacesContext.getCurrentInstance().addMessage(null, facesMessage);
                    return false;
                }
                if (nombreDeArchivo2.endsWith(".jpg") || nombreDeArchivo2.endsWith(".jpeg")) {
                    extensionFoto = ".jpg";
                } else if (nombreDeArchivo2.endsWith(".png")) {
                    extensionFoto = ".png";
                } else {
                    FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Formato invalido : Foto 1", null);
                    FacesContext.getCurrentInstance().addMessage(null, facesMessage);
                    return false;
                }
                String nombreDeArchivo = "puesto/" + p.getId() + "1" + extensionFoto;
                ManejadorDeImagenes mdi = new ManejadorDeImagenes();
                mdi.escribirImagen(up, nombreDeArchivo);
                Fotospuesto f = new Fotospuesto(new FotospuestoPK(p.getId(), nombreDeArchivo));
                new FabricaControladorJpa().obtenerControladorJpaFotospuesto().create(f);
                return true;
            }
        } catch (Exception e) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
        return false;
    }
}
