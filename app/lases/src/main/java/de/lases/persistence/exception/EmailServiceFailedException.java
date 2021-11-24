package de.lases.persistence.exception;

import java.io.Serial;

/**
 * Is thrown when the email service used by the application fails unrecoverably.
 */
public class EmailServiceFailedException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -1123989697391542082L;

    /**
     * Constructs an exception.
     */
    public EmailServiceFailedException() {
    }

    /**
     * Constructs an exception with a message.
     *
     * @param message Error message.
     */
    public EmailServiceFailedException(String message) {
        super(message);
    }

    /**
     * Constructs an exception with a message and a cause.
     *
     * @param message Error message.
     * @param cause Cause of the exception.
     */
    public EmailServiceFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
