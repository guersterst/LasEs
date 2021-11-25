package de.lases.persistence.exception;

import java.io.Serial;

/**
 * Is thrown when the repository tries to save data with a unique value that
 * already exists in the data.
 */
public class KeyExistsException extends Exception {

    @Serial
    private static final long serialVersionUID = 4050043739568595650L;

    /**
     * Constructs an exception.
     */
    public KeyExistsException() {
    }

    /**
     * Constructs an exception with a message.
     *
     * @param message Error message.
     */
    public KeyExistsException(String message) {
        super(message);
    }

    /**
     * Constructs an exception with a message and a cause.
     *
     * @param message Error message.
     * @param cause Cause of the exception.
     */
    public KeyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
