package de.lases.persistence.exception;

import java.io.Serial;

/**
 * Is thrown when a resource (like a connection pool) is depleted.
 */
public class DepletedResourceException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 8679228847181645272L;

    /**
     * Constructs an exception.
     */
    public DepletedResourceException() {
    }

    /**
     * Constructs an exception with a message.
     *
     * @param message Error message.
     */
    public DepletedResourceException(String message) {
        super(message);
    }

    /**
     * Constructs an exception with a message and a cause.
     *
     * @param message Error message.
     * @param cause Cause of the exception.
     */
    public DepletedResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
