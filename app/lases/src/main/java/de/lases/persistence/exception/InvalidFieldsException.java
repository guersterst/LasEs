package de.lases.persistence.exception;

import java.io.Serial;

/**
 * Is thrown when there is an invalid field in a dto given to the persistence
 * layer.
 */
public class InvalidFieldsException extends IllegalArgumentException {

    @Serial
    private static final long serialVersionUID = 7556671731117466807L;

    /**
     * Constructs an exception.
     */
    public InvalidFieldsException() {
        super();
    }

    /**
     * Constructs an exception with a message.
     *
     * @param s Error message.
     */
    public InvalidFieldsException(String s) {
        super(s);
    }

    /**
     * Constructs an exception with a message and a cause.
     *
     * @param message Error message.
     * @param cause Cause of the exception.
     */
    public InvalidFieldsException(String message, Throwable cause) {
        super(message, cause);
    }
}
