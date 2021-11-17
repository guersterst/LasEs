package de.lases.persistence.exception;

import java.io.Serial;

/**
 * Is thrown when a query to the datasource fails and will probably fail again.
 */
public class DatasourceQueryFailedException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 8679228847181645272L;
}
