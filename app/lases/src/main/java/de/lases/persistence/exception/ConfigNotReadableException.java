package de.lases.persistence.exception;

import java.io.Serial;

/**
 * Fires when the config file of the application can definitively not be read.
 */
public class ConfigNotReadableException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1459528496688127008L;

    /**
     * Constructs an exception with a message and a cause.
     *
     * @param message Error message.
     * @param cause Cause of the exception.
     */
    public ConfigNotReadableException(String message, Throwable cause) {
        super(message, cause);
    }
}
