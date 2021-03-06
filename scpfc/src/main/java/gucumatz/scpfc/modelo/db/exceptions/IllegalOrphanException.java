package gucumatz.scpfc.modelo.db.exceptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Excepción que indica que realizar una operación dejaría sin padre a una
 * entidad en la base.
 */
@SuppressWarnings("checkstyle:linelength")
public class IllegalOrphanException extends Exception {

    private List<String> messages;

    public IllegalOrphanException(List<String> messages) {
        super((messages != null && messages.size() > 0 ? messages.get(0) : null));
        if (messages == null) {
            this.messages = new ArrayList<String>();
        } else {
            this.messages = messages;
        }
    }

    public List<String> getMessages() {
        return messages;
    }
}
