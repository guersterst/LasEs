package de.lases.persistence.exception;

import java.io.Serial;

/**
 * Hints at invalid field in the ResulstListParameters object given to the
 * persistence layer.
 */
public class InvalidQueryParamsException extends IllegalArgumentException {

    @Serial
    private static final long serialVersionUID = -7077745141511258612L;
}
