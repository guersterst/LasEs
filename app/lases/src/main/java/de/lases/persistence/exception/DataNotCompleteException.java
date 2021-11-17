package de.lases.persistence.exception;

import java.io.Serial;

/**
 * Is thrown when the reading of data yields an incomplete result but a retry
 * has a high probability of succeeding, for example when a DataTruncation
 * warning occurs.
 */
public class DataNotCompleteException extends Exception {

    @Serial
    private static final long serialVersionUID = 695814409120288262L;
}
