package de.lases.persistence.exception;

import java.io.Serial;

/**
 * Is thrown when the datasource cannot be found. For example when a database
 * connection cannot be established or times out.
 */
public class DatasourceNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -2008534506407499953L;
}
