package de.lases.persistence.exception;

import java.io.Serial;

/**
 * Is thrown when a queried object is not found in the database. In the common
 * case this is caused by a race condition where an object was removed while
 * being selected.
 */
public class NotFoundException extends Exception {

    @Serial
    private static final long serialVersionUID = -8622898374545070975L;

    /**
     * Constructs an exception with a message and a cause.
     *
     * @param message Error message.
     * @param cause Cause of the exception.
     */
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
