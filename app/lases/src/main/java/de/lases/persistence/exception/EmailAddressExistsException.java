package de.lases.persistence.exception;

import java.io.Serial;

/**
 * Is thrown when the repository tries to save an email-address to the database
 * that already exists in the database.
 */
public class EmailAddressExistsException extends Exception {

    @Serial
    private static final long serialVersionUID = 4050043739568595650L;
}
