package de.lases.business.util;


import de.lases.global.transport.*;
import de.lases.persistence.exception.*;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

/**
 * Provides functionality for sending emails.
 */
public class EmailUtil {

    @Inject
    private Event<UIMessage> uiMessageEvent;


    /**
     * Sends an email to a given recipient.
     *
     * @param sender     The senders email address.
     * @param recipients The recipients email addresses.
     * @param cc         The recipients in carbon-copy.
     * @param subject    The subject of the email.
     * @param body       The body of the email.
     * @throws EmailTransmissionFailedException Thrown when an issue with the transmission of the email has occurred.
     */
    public static void sendEmail(String sender, String[] recipients, String[] cc, String subject, String body)
            throws EmailTransmissionFailedException {
    }

    /**
     * Generates a mailto link.
     *
     * @param recipients The recipients email addresses.
     * @param cc         The recipients in carbon-copy.
     * @param subject    The subject of the email.
     * @param body       The body of the email.
     * @return A link leading to a mailto e-mail.
     */
    public static String generateMailToLink(String[] recipients, String[] cc, String subject, String body) {
        return null;
    }

    /**
     * Determines whether an e-mail address has already been registered in the application.
     *
     * @param address The address to be checked.
     * @return {@code true} if the email is already in use, {@code false} otherwise.
     */
    public static boolean isEmailUsed(String address) {
        return false;
    }

}
