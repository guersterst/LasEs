package de.lases.persistence.exception;

import java.io.Serial;

/**
 * Is thrown when there is an invalid field in the ResultListParameters object
 * given to the persistence layer.
 */
public class InvalidQueryParamsException extends IllegalArgumentException {

    @Serial
    private static final long serialVersionUID = -7077745141511258612L;

    /**
     * Constructs an exception.
     */
    public InvalidQueryParamsException() {
    }

    /**
     * Constructs an exception with a message.
     *
     * @param s Error messge.
     */
    public InvalidQueryParamsException(String s) {
        super(s);
    }

    /**
     * Constructs an exception with a message and a cause.
     *
     * @param message Error message.
     * @param cause Cause of the exception.
     */
    public InvalidQueryParamsException(String message, Throwable cause) {
        super(message, cause);
    }
}
