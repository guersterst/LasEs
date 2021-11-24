package de.lases.persistence.exception;

import java.io.Serial;

/**
 * Is thrown when the logger cannot write log files.
 */
public class LogsNotWritableException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -4400360015042796433L;

    /**
     * Constructs an exception.
     */
    public LogsNotWritableException() {
    }

    /**
     * Constructs an exception with a message.
     *
     * @param message Error message.
     */
    public LogsNotWritableException(String message) {
        super(message);
    }

    /**
     * Constructs an exception with a message and a cause.
     *
     * @param message Error message.
     * @param cause Cause of the exception.
     */
    public LogsNotWritableException(String message, Throwable cause) {
        super(message, cause);
    }
}
