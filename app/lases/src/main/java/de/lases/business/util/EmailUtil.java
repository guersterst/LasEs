package de.lases.business.util;


import de.lases.global.transport.*;
import de.lases.persistence.exception.*;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

/**
 * Provides functionality for sending emails.
 */
public class EmailUtil {

    //todo flache params.

    @Inject
    private Event<UIMessage> uiMessageEvent;

    //todo mehrere recipients
    /**
     * Sends an email to a given recipient.
     *
     * @param sender The senders email address.
     * @param recipient The recipients email address.
     * @param subject The subject of the email.
     * @param body The body of the email.
     * @throws EmailTransmissionFailedException Thrown when an issue with the transmission of the email has occurred.
     */
    public static void sendEmail(String sender, String recipient, String subject, String body) throws EmailTransmissionFailedException {
    }

    //todo was ist return?
    /**
     *
     * @param receiver
     * @param cc
     * @param subject
     * @param body
     * @return
     */
    public static String generateMailTo(String[] receiver, String[] cc, String subject, String body) {
        return null;
    }

    // todo what this?
    public static boolean checkEmailAddress(String address) {
        return false;
    }

}
