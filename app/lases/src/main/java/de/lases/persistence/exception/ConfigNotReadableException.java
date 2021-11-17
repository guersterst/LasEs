package de.lases.persistence.exception;

import java.io.Serial;

/**
 * Fires when the config file of the application can definitively not be read.
 */
public class ConfigNotReadableException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1459528496688127008L;
}
