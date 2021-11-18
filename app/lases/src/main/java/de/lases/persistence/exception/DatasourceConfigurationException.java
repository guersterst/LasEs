package de.lases.persistence.exception;

import java.io.Serial;

/**
 * Is thrown when bad configuration of the datasource leads to a failure to
 * fetch data.
 */
public class DatasourceConfigurationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -2193983203293840403L;

    /**
     * Constructs an exception with a message and a cause.
     *
     * @param message Error message.
     * @param cause Cause of the exception.
     */
    public DatasourceConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
