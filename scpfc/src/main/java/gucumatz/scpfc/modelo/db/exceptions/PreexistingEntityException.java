package gucumatz.scpfc.modelo.db.exceptions;

/**
 * Excepción que indica que la entidad ya existe en la base de datos.
 */
public class PreexistingEntityException extends Exception {

    public PreexistingEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public PreexistingEntityException(String message) {
        super(message);
    }
}
