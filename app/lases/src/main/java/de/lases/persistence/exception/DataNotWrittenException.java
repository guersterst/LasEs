package de.lases.persistence.exception;

import java.io.Serial;

/**
 * Is thrown when the writing of data fails but a retry has a high probability
 * of succeeding, for example when a DataTruncationException occurs.
 */
public class DataNotWrittenException extends Exception {

    @Serial
    private static final long serialVersionUID = 7618083028127586109L;
}
