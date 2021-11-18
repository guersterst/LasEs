package de.lases.persistence.exception;

import java.io.Serial;

/**
 * Hints at an invalid field in a dto given to the persistence layer.
 */
public class InvalidFieldsException extends IllegalArgumentException {

    @Serial
    private static final long serialVersionUID = 7556671731117466807L;

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
