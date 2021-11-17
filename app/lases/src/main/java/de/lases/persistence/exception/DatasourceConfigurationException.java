package de.lases.persistence.exception;

import java.io.Serial;

/**
 * Is thrown when bad configuration of the datasource leads to a failure to
 * fetch data.
 */
public class DatasourceConfigurationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -2193983203293840403L;
}
