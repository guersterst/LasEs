package de.lases.business.util;


import de.lases.global.transport.*;
import de.lases.persistence.exception.*;
import jakarta.enterprise.event.Event;

public class EmailUtil {

    private Event<UIMessage> uiMessageEvent;

    public static void sendEmail(String sender, String recipient, String subject, String body) throws EmailTransmissionFailedException {
    }

    public static String generateMailTo(String[] receiver, String[] cc, String subject, String body) {
        return null;
    }

    public static boolean checkEmailAddress(String address) {
        return false;
    }

}
