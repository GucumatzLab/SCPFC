package gucumatz.scpfc.controlador;

import gucumatz.scpfc.modelo.Usuario;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.NoneScoped;
import javax.faces.context.ExternalContext;
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
    private String directorio;

    public ManejadorDeImagenes() throws IOException {
        directorio = null;

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        String archivoPropiedades = "WEB-INF/imagenes.properties";
        try (InputStream is
                = externalContext.getResourceAsStream(archivoPropiedades)) {
            Properties prop = new Properties();
            prop.load(is);
            directorio = prop.getProperty("raiz-imagenes");
        }
        Files.createDirectories(Paths.get(directorio));
    }

    /**
     * Regresa una imagen del disco como StreamedContent.
     *
     * @param archivo el nombre del archivo donde se encuentra la imagen
     * @return la imagen con un formato adecuado para p:graphicImage
     * @throws IOException si ocurre un error al leer la imagen
     */
    public StreamedContent obtenerImagenDeArchivo(String archivo)
            throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        if (facesContext.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            return new DefaultStreamedContent();
        } else {
            InputStream in = new FileInputStream(new File(directorio, archivo));
            return new DefaultStreamedContent(in);
        }
    }

    /**
     * Obtiene una imagen desde un archivo. El nombre de la imagen se obtiene
     * como un parámetro en el contexto con nombre "ruta".
     *
     * @return la imagen con un formato adecuado para p:graphicImage
     * @throws IOException si ocurre un error al leer la imagen
     */
    public StreamedContent getImagenDeArchivo() throws IOException {
        String rutaImagen
            = FacesContext.getCurrentInstance().getExternalContext()
                .getRequestParameterMap().get("ruta");
        return obtenerImagenDeArchivo(rutaImagen);
    }

    /**
     * Regresa un recurso de la aplicación como imagen.
     *
     * @param recurso el nombre del recurso que contiene a la imagen
     * @return el recurso como una imagen adecuada para p:graphicImage
     * @throws IOException si ocurre un error al leer la imagen
     */
    public StreamedContent obtenerImagenDeRecurso(String recurso)
            throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        if (facesContext.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            /* No hacemos nada si está generando la vista. */
            return new DefaultStreamedContent();
        } else {
            InputStream in = facesContext.getExternalContext()
                .getResourceAsStream(recurso);
            return new DefaultStreamedContent(in);
        }
    }

    /**
     * Escribe una imagen a un archivo en el directorio adecuado.
     *
     * @param archivo la imagen que se quiere guardar
     * @param nombre el nombre con el que se guardará la imagen
     * @throws IOException si ocurre un error al escribir la imagen
     */
    public void escribirImagen(UploadedFile archivo, String nombre)
            throws IOException {
        Path rutaArchivo = Paths.get(directorio, nombre);
        Path directorioImagen = rutaArchivo.getParent();
        try (InputStream in = archivo.getInputstream()) {
            Files.createDirectories(directorioImagen);
            Files.copy(in, rutaArchivo);
        }
    }

    /**
     * Regresa la foto que se asigna a los usuarios que no suben una propia.
     *
     * @return la foto de usuario por omisión
     * @throws IOException si ocurre un error al leer la foto
     */
    public StreamedContent obtenerFotoDeUsuarioPorOmision() throws IOException {
        return obtenerImagenDeRecurso("/imagenes/usuario.png");
    }

    /**
     * Regresa la foto de un usuario. Si no tiene, regresa una por defecto.
     * Obtiene la ruta de la foto como parámetro en el contexto con nombre
     * "rutaImagen".
     *
     * @return la foto del usuario en un formato adecuado para p:graphicImage
     * @throws IOException si hay un error al leer la imagen
     */
    public StreamedContent getFotoDeUsuario() throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String rutaImagen = facesContext.getExternalContext()
            .getRequestParameterMap().get("rutaImagen");
        if (rutaImagen == null || rutaImagen.equals("")) {
            return obtenerFotoDeUsuarioPorOmision();
        }
        return obtenerImagenDeArchivo(rutaImagen);
    }

    /**
     * Regresa la foto de un usuario. Si no tiene o recibe null, regresa una por
     * defecto.
     *
     * @param usuario el usuario cuya foto se quiere obtener
     * @return la foto del usuario en un formato adecuado para p:graphicImage
     * @throws IOException si hay un error al leer la imagen
     */
    public StreamedContent getFotoDeUsuario(Usuario usuario)
            throws IOException {
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
