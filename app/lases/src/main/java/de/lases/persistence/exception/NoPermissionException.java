package de.lases.persistence.exception;

import java.io.Serial;

/**
 * Is thrown when a user does not have the correct role to complete a certain
 * operation. This was most likely caused by a race condition where someone
 * lost a role during completion of an operation.
 */
public class NoPermissionException extends Exception {

    @Serial
    private static final long serialVersionUID = 4189764514650645309L;

    /**
     * Constructs an exception.
     */
    public NoPermissionException() {
    }

    /**
     * Constructs an exception with a message.
     *
     * @param message Error message.
     */
    public NoPermissionException(String message) {
        super(message);
    }

    /**
     * Constructs an exception with a message and a cause.
     *
     * @param message Error message.
     * @param cause Cause of the exception.
     */
    public NoPermissionException(String message, Throwable cause) {
        super(message, cause);
    }
}
