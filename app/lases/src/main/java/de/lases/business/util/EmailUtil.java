package de.lases.business.util;


import de.lases.business.internal.ConfigPropagator;
import de.lases.persistence.exception.EmailTransmissionFailedException;
import de.lases.persistence.util.EmailSender;
import jakarta.enterprise.inject.spi.CDI;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Provides functionality for sending emails.
 *
 * @author Thomas Kirz
 */
public class EmailUtil {

    /**
     * Sends an email to a given recipient.
     *
     * @param recipients The recipients email addresses.
     * @param cc         The recipients in carbon-copy.
     * @param subject    The subject of the email.
     * @param body       The body of the email.
     * @throws EmailTransmissionFailedException Thrown when an issue with the transmission of the email has occurred.
     */
    public static void sendEmail(String[] recipients, String[] cc, String subject, String body)
            throws EmailTransmissionFailedException {
        ConfigPropagator config = CDI.current().select(ConfigPropagator.class).get();
        EmailSender.sendEmail(config.getProperty("MAIL_ADDRESS_FROM"), recipients, cc, subject, body);
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
        String mailto= "mailto:" + String.join(",", recipients);
        String delimiter = "?";
        if (subject != null) {
            mailto += delimiter + "subject=" + URLEncoder.encode(subject, StandardCharsets.UTF_8);
            delimiter = "&";
        }
        if (body != null) {
            // mailto links use \r\n as line endings
            body = body.replaceAll("([^\\r])\\n", "$1\r\n");
            mailto += delimiter + "body=" + URLEncoder.encode(body, StandardCharsets.UTF_8);
            delimiter = "&";
        }
        if (cc != null) {
            mailto += delimiter + "cc=" + String.join(",", cc);
        }
        return mailto;
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
