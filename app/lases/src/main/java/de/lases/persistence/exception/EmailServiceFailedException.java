package de.lases.persistence.exception;

import java.io.Serial;

/**
 * Fires when the email service used by the application fails unrecoverably.
 */
public class EmailServiceFailedException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -1123989697391542082L;
}
