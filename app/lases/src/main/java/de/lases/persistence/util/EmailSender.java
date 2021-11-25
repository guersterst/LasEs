package de.lases.persistence.util;

import de.lases.persistence.exception.EmailServiceFailedException;
import de.lases.persistence.exception.EmailTransmissionFailedException;

/**
 * Utility class for sending emails.
 */
public class EmailSender {

    /**
     * Send an email.
     *
     * @param sender The sender email address.
     * @param recipient The recipient email address.
     * @param subject The subject of the email.
     * @param body The body of the email.
     * @throws EmailTransmissionFailedException If the transmission of this
     *                                          email fails due to a
     *                                          recoverable error in the email
     *                                          service.
     * @throws EmailServiceFailedException If the transmission of this email
     *                                     fails due to a non-recoverable error
     *                                     in the email service.
     */
    public static void sendEmail(String sender, String recipient,
                                 String subject, String body)
            throws EmailTransmissionFailedException {
    }

}
