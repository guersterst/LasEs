package de.lases.persistence.exception;

import java.io.Serial;

/**
 * Is thrown when the logger cannot write log files.
 */
public class LogsNotWritableException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -4400360015042796433L;
}
