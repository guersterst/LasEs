package de.lases.persistence.exception;

import java.io.Serial;

/**
 * Is thrown when a user does not have the correct role to complete a certain
 * operation. This was most likely caused by a race condition where someone
 * lost a role during completion of an operation.
 */
public class NoPermissionException extends Exception {

    @Serial
    private static final long serialVersionUID = 4189764514650645309L;
}
