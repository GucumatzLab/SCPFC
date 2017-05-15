package gucumatz.scpfc.controlador;

import gucumatz.scpfc.modelo.db.*;
import gucumatz.scpfc.modelo.Puesto;
import gucumatz.scpfc.modelo.*;
import java.util.Locale;
import java.util.LinkedList;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.UploadedFile;

/**
 * Clase para controlar un objeto de la Clase Puesto
 *
 * @author Rivera Lopez Jorge Erick
 * @version 0.1
 */
@ManagedBean
@ViewScoped

public class AgregadorPuesto implements java.io.Serializable {

    private final ControladorJpaPuesto jpaPuesto;
    private final ControladorJpaFotoPuesto jpaFotoPuesto;
    private String am1;
    private String am2;
    private String pm1;
    private String pm2;
    private UploadedFile foto1;
    private UploadedFile foto2;
    private UploadedFile foto3;
    private Puesto puesto = new Puesto();

    /**
     * Constructor de la clase AgregadorPuesto
     */
    public AgregadorPuesto() {
        FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale("es-Mx"));
        jpaPuesto = new FabricaControladorJpa().obtenerControladorJpaPuesto();
        jpaFotoPuesto = new FabricaControladorJpa().obtenerControladorJpaFotoPuesto();
    }

    /**
     * Método para obtener el puesto de la clase.
     *
     * @return puesto.
     */
    public Puesto getPuesto() {
        return puesto;
    }

    /**
     * Metodo para cambiar el valor de Puesto
     *
     * @param puesto - puesto con el que se reemplazara
     */
    public void setPuesto(Puesto puesto) {
        this.puesto = puesto;
    }

    /**
     * Metodo para obtener la foto 1.
     *
     * @return foto 1.
     */
    public UploadedFile getFoto1() {
        return this.foto1;
    }

    /**
     * Metodo para cambiar la foto 1.
     *
     * @param nuevo - nueva foto.
     */
    public void setFoto1(UploadedFile nuevo) {
        this.foto1 = nuevo;
    }

    /**
     * Metodo para obtener la foto 2.
     *
     * @return foto 2.
     */
    public UploadedFile getFoto2() {
        return this.foto2;
    }

    /**
     * Metodo para cambiar la foto 2.
     *
     * @param nuevo - nueva foto.
     */
    public void setFoto2(UploadedFile nuevo) {
        this.foto2 = nuevo;
    }

    /**
     * Metodo para obtener la foto 3.
     *
     * @return foto 3.
     */
    public UploadedFile getFoto3() {
        return this.foto3;
    }

    /**
     * Metodo para cambiar la foto 3.
     *
     * @param nuevo - nueva foto.
     */
    public void setFoto3(UploadedFile nuevo) {
        this.foto3 = nuevo;
    }

    /**
     * Hora AM
     *
     * @return am1
     */
    public String getAm1() {
        return this.am1;
    }

    /**
     * Metodo para cambiar Hora AM
     *
     * @param nuevo - nueva hora AM
     */
    public void setAm1(String nuevo) {
        this.am1 = nuevo;
    }

    /**
     * Minuto AM
     *
     * @return am2
     */
    public String getAm2() {
        return this.am2;
    }

    /**
     * Metodo para cambiar Minuto AM
     *
     * @param nuevo - nuevo minuto AM
     */
    public void setAm2(String nuevo) {
        this.am2 = nuevo;
    }

    /**
     * Hora PM
     *
     * @return pm1
     */
    public String getPm1() {
        return this.pm1;
    }

    /**
     * Metodo para cambiar Hora PM
     *
     * @param nuevo - nueva hora PM
     */
    public void setPm1(String nuevo) {
        this.pm1 = nuevo;
    }

    /**
     * Minuto PM
     *
     * @return am1
     */
    public String getPm2() {
        return this.pm2;
    }

    /**
     * Metodo para cambiar minuto PM
     *
     * @param nuevo - nuevo minuto AM
     */
    public void setPm2(String nuevo) {
        this.pm2 = nuevo;
    }

    /**
     * Metodo con el que se hace la peticion al controlador para agregar un
     * Puesto a la base de datos
     */
    public void agrega() {
        // Hacer el trim
        this.puesto.setNombre(this.puesto.getNombre().trim());
        this.puesto.setTipoComida(this.puesto.getTipoComida().trim());

        // Validar
        try {
            if (validaNombre(this.puesto.getNombre(), this.puesto.getTipoComida())) {
                if (this.puesto.getUbicacion().length() == 0) {
                    this.puesto.setUbicacion("Ninguna Referencia");
                }
                if (this.puesto.getLatitud() == 0 && this.puesto.getLongitud() == 0) {
                    FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Selecciona la Localizacion", null);
                    FacesContext.getCurrentInstance().addMessage(null, facesMessage);
                    return;
                }
                int a1 = (int) Double.parseDouble(this.am1);
                int a2 = (int) Double.parseDouble(this.am2);
                int p1 = (int) Double.parseDouble(this.pm1);
                int p2 = (int) Double.parseDouble(this.pm2);
                if (a1 < 10) {
                    this.am1 = "0" + a1;
                } else {
                    this.am1 = Integer.toString(a1);
                }
                if (a2 < 10) {
                    this.am2 = "0" + a2;
                } else {
                    this.am2 = Integer.toString(a2);
                }
                if (p1 < 10) {
                    this.pm1 = "0" + p1;
                } else {
                    this.pm1 = Integer.toString(p1);
                }
                if (p2 < 10) {
                    this.pm2 = "0" + p2;
                } else {
                    this.pm2 = Integer.toString(p2);
                }
                this.puesto.setHorario("Abierto desde las " + this.am1 + ":" + this.am2 + " hrs. a las " + this.pm1 + ":" + this.pm2 + " hrs.");
                this.puesto.setFotosPuesto(new LinkedList<FotoPuesto>());
                jpaPuesto.crear(this.puesto);

                if (foto1 != null
                        && foto1.getSize() > 0
                        && !guardaImagen(this.foto1, 1)) {
                    for (FotoPuesto foto : this.puesto.getFotosPuesto()) {
                        jpaFotoPuesto.destruir(foto.getId());
                    }
                    jpaPuesto.destruir(this.puesto.getId());
                    return;
                }
                if (foto2 != null
                        && foto2.getSize() > 0
                        && !guardaImagen(this.foto2, 2)) {
                    for (FotoPuesto foto : this.puesto.getFotosPuesto()) {
                        jpaFotoPuesto.destruir(foto.getId());
                    }
                    jpaPuesto.destruir(this.puesto.getId());
                    return;
                }
                if (foto3 != null
                        && foto3.getSize() > 0
                        && !guardaImagen(this.foto3, 3)) {
                    for (FotoPuesto foto : this.puesto.getFotosPuesto()) {
                        jpaFotoPuesto.destruir(foto.getId());
                    }
                    jpaPuesto.destruir(this.puesto.getId());
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

    /**
     * Metodo para validar un nombre de puesto.
     *
     * @param nombrePuesto - nombre del puesto a agregar.
     * @param tipoComida - tipo de comida que se vende en el puesto.
     * @return Devuelve true si el nombre esta disponible.
     */
    public boolean validaNombre(String nombrePuesto, String tipoComida) {
        Puesto p = jpaPuesto.buscarPorNombre(nombrePuesto);
        if (p != null) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR\nEl nombre ya existe", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return false;
        }

        if (nombrePuesto.equals("")) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR\nNo se puede tener un nombre vacío.", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return false;
        }

        if (tipoComida.equals("")) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR\nNo se puede tener un tipo de comida vacío.", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return false;
        }

        return true;
    }

    /**
     * Metodo que redirecciona la página actual a Administrar.xhtml.
     */
    public void redirecciona() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("./Administrar.xhtml");
        } catch (Exception e) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR 01" + e.toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
    }

    /**
     * Metodo que guarda una imagen en la memoria del equipo y la base de datos.
     *
     * @param up - archivo a guardar.
     * @param id - numero de archivo.
     * @return Devuelve true si no hubo problemas al guardar los datos.
     */
    public boolean guardaImagen(UploadedFile up, int id) {
        try {
            if (up != null) {
                String extensionFoto = null;
                String nombreDeArchivo2 = up.getFileName();
                if (up.getSize() > (4 * 1024 * 1024)) {
                    FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Tamaño invalido : Foto " + id + ", puedes cambiar la foto más tarde", null);
                    FacesContext.getCurrentInstance().addMessage(null, facesMessage);
                    return false;
                }
                if (nombreDeArchivo2.endsWith(".jpg") || nombreDeArchivo2.endsWith(".jpeg")) {
                    extensionFoto = ".jpg";
                } else if (nombreDeArchivo2.endsWith(".png")) {
                    extensionFoto = ".png";
                } else {
                    FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Formato invalido : Foto 1, puedes cambiar la foto más tarde", null);
                    FacesContext.getCurrentInstance().addMessage(null, facesMessage);
                    return false;
                }
                String nombreDeArchivo = "puesto/" + puesto.getId() + "-" + id + extensionFoto;
                ManejadorDeImagenes mdi = new ManejadorDeImagenes();
                mdi.escribirImagen(up, nombreDeArchivo);
                FotoPuesto f = new FotoPuesto();
                f.setUrl(nombreDeArchivo);
                f.setPuesto(puesto);
                puesto.getFotosPuesto().add(f);
                jpaFotoPuesto.crear(f);
                return true;
            }
        } catch (Exception e) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR Al tratar de subir la foto " + e.getMessage(), null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
        return false;
    }
}
