package de.lases.persistence.exception;

import java.io.Serial;

/**
 * Is thrown when a query to the datasource fails and will probably fail again.
 */
public class DatasourceQueryFailedException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 8679228847181645272L;

    /**
     * Constructs an exception.
     */
    public DatasourceQueryFailedException() {
    }

    /**
     * Constructs an exception with a message.
     *
     * @param message Error message.
     */
    public DatasourceQueryFailedException(String message) {
        super(message);
    }

    /**
     * Constructs an exception with a message and a cause.
     *
     * @param message Error message.
     * @param cause Cause of the exception.
     */
    public DatasourceQueryFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
