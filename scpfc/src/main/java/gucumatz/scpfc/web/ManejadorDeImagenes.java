/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gucumatz.scpfc.web;

import gucumatz.scpfc.modelo.Usuario;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.NoneScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 * Clase para manejar imágenes. Permite leer y escribir imágenes al disco, leer
 * recursos como imágenes, y generar objetos de tipo StreamedContent desde las
 * imágenes leidas.
 *
 * @author lchacon
 */
@ManagedBean
@NoneScoped
public class ManejadorDeImagenes {

    /**
     * Directorio donde se guardan las imágenes.
     */
    private final String directorio = "/tmp/scpfc/imagenes/";

    public ManejadorDeImagenes() throws IOException {
        Files.createDirectories(Paths.get(directorio));
    }

    /**
     * Regresa una imagen del disco como StreamedContent.
     */
    public StreamedContent obtenerImagenDeArchivo(String archivo) throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        if (facesContext.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            return new DefaultStreamedContent();
        } else {
            InputStream in = new FileInputStream(new File(directorio, archivo));
            return new DefaultStreamedContent(in);
        }
    }

    public StreamedContent getImagenDeArchivo() throws IOException {
        String rutaImagen = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("ruta");
        return obtenerImagenDeArchivo(rutaImagen);
    }

    /**
     * Regresa un recurso de la aplicación como imagen.
     */
    public StreamedContent obtenerImagenDeRecurso(String recurso) throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        if (facesContext.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            /* No hacemos nada si está generando la vista. */
            return new DefaultStreamedContent();
        } else {
            InputStream in = facesContext.getExternalContext().getResourceAsStream(recurso);
            return new DefaultStreamedContent(in);
        }
    }

    /**
     * Escribe una imagen a un archivo en el directorio adecuado.
     */
    public void escribirImagen(UploadedFile archivo, String nombre) throws IOException {
        Path rutaArchivo = Paths.get(directorio, nombre);
        Path directorio = rutaArchivo.getParent();
        try (InputStream in = archivo.getInputstream()) {
            Files.createDirectories(directorio);
            Files.copy(in, rutaArchivo);
        }
    }

    public StreamedContent obtenerFotoDeUsuarioPorOmision() throws IOException {
        return obtenerImagenDeRecurso("/imagenes/usuario.png");
    }

    /**
     * Regresa la foto de un usuario. Si no tiene, regresa una por defecto.
     */
    public StreamedContent getFotoDeUsuario() throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String rutaImagen = facesContext.getExternalContext().getRequestParameterMap().get("rutaImagen");
        if (rutaImagen == null || rutaImagen.equals("")) {
            return obtenerFotoDeUsuarioPorOmision();
        }
        return obtenerImagenDeArchivo(rutaImagen);
    }

    /**
     * Regresa la foto de un usuario. Si no tiene o recibe null, regresa una por
     * defecto.
     */
    public StreamedContent getFotoDeUsuario(Usuario usuario) throws IOException {
        String rutaImagen = null;
        if (usuario != null) {
            rutaImagen = usuario.getRutaImagen();
        }
        if (rutaImagen == null || rutaImagen.equals("")) {
            return obtenerFotoDeUsuarioPorOmision();
        }
        return obtenerImagenDeArchivo(rutaImagen);
    }
}
