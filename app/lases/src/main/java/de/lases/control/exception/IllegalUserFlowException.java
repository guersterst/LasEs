package de.lases.control.exception;

import java.io.Serial;

/**
 * Gets thrown when a user accesses a page that can only be accessed through
 * a specific link/user flow directly over the URL.
 */
public class IllegalUserFlowException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 5268923976706134722L;

    /**
     * Constructs an exception.
     */
    public IllegalUserFlowException() {
    }

    /**
     * Constructs an exception with a message.
     *
     * @param message Error message.
     */
    public IllegalUserFlowException(String message) {
        super(message);
    }
}
