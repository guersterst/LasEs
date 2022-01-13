package de.lases.persistence.exception;

import java.io.Serial;

/**
 * Is thrown whenever the transmission of an email (to a specific list of
 * senders) fails but the email service of this system is intact.
 */
public class EmailTransmissionFailedException extends Exception {

    @Serial
    private static final long serialVersionUID = -8882815379970644060L;

    private String[] invalidAddresses;

    /**
     * Constructs an exception.
     */
    public EmailTransmissionFailedException() {
        super();
    }

    /**
     * Constructs an exception with a message.
     *
     * @param message Error message.
     */
    public EmailTransmissionFailedException(String message) {
        super(message);
    }

    /**
     * Constructs an exception with a message and a cause.
     *
     * @param message Error message.
     * @param cause   Cause of the exception.
     */
    public EmailTransmissionFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs an exception with a message, a cause and a list of addresses that were not sent to.
     *
     * @param message Error message.
     * @param invalidAddresses List of addresses that were not sent to.
     */
    public EmailTransmissionFailedException(String message, Throwable cause, String[] invalidAddresses) {
        super(message, cause);
        this.invalidAddresses = invalidAddresses;
    }

    public String[] getInvalidAddresses() {
        return invalidAddresses;
    }
}
