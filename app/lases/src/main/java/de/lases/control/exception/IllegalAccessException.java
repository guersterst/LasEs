package de.lases.control.exception;

import java.io.Serial;

/**
 * Is thrown when a user tries to access a page he is not entitled to visit.
 */
public class IllegalAccessException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -7108331422615826777L;

    /**
     * Constructs an exception with a message.
     *
     * @param message Error message.
     */
    public IllegalAccessException(String message) {
        super(message);
    }
}
