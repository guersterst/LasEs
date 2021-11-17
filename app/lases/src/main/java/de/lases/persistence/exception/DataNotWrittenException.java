package de.lases.persistence.exception;

import java.io.Serial;

/**
 * Is thrown when the writing of data fails but a retry has a high probability
 * of succeeding, for example when a DataTruncationException occurs.
 */
public class DataNotWrittenException extends Exception {

    @Serial
    private static final long serialVersionUID = 7618083028127586109L;

    /**
     * Constructs an exception with a message and a cause.
     *
     * @param message Error message.
     * @param cause Cause of the exception.
     */
    public DataNotWrittenException(String message, Throwable cause) {
        super(message, cause);
    }
}
