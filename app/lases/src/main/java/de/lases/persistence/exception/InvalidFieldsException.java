package de.lases.persistence.exception;

import java.io.Serial;

/**
 * Hints at an invalid field in a dto given to the persistence layer.
 */
public class InvalidFieldsException extends IllegalArgumentException {

    @Serial
    private static final long serialVersionUID = 7556671731117466807L;
}
