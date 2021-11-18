package de.lases.persistence.exception;

import java.io.Serial;

/**
 * Fires whenever the transmission of an email (to a specific list of senders)
 * fails but the email service of this system is intact.
 */
public class EmailTransmissionFailedException extends Exception {

    @Serial
    private static final long serialVersionUID = -8882815379970644060L;

    /**
     * Constructs an exception with a message and a cause.
     *
     * @param message Error message.
     * @param cause Cause of the exception.
     */
    public EmailTransmissionFailedException(String message, Throwable cause) {
        super(message, cause);
    }

}
