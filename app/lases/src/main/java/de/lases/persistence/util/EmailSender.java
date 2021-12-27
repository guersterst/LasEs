package de.lases.persistence.util;

import com.sun.mail.util.MailConnectException;
import de.lases.persistence.exception.EmailServiceFailedException;
import de.lases.persistence.exception.EmailTransmissionFailedException;
import de.lases.persistence.internal.ConfigReader;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.mail.*;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;
import java.util.logging.Logger;

/**
 * Utility class for sending emails.
 *
 * @author Thomas Kirz
 */
public class EmailSender {

    private static final Logger logger = Logger.getLogger(EmailSender.class.getName());

    /**
     * Send an email.
     *
     * @param sender     The sender email address.
     * @param recipients The recipient email addresses.
     * @param cc         The cc email addresses.
     * @param subject    The subject of the email.
     * @param body       The body of the email.
     * @throws EmailTransmissionFailedException If the transmission of this
     *                                          email fails due to a
     *                                          recoverable error in the email
     *                                          service.
     * @throws EmailServiceFailedException      If the transmission of this email
     *                                          fails due to a non-recoverable error
     *                                          in the email service.
     */
    public static void sendEmail(String sender, String[] recipients, String[] cc, String subject, String body)
            throws EmailTransmissionFailedException {
        // Read the email configuration from the config file.
        ConfigReader config = CDI.current().select(ConfigReader.class).get();
        Properties props = new Properties();
        props.setProperty("mail.smtp.host", config.getProperty("mail.smtp.host"));
        props.setProperty("mail.smtp.port", config.getProperty("mail.smtp.port"));
        props.setProperty("mail.smtp.auth", config.getProperty("mail.smtp.auth"));
        props.setProperty("mail.smtp.starttls.enable", config.getProperty("mail.smtp.starttls.enable"));

        Session session;
        if (config.getProperty("mail.smtp.auth").equals("true")) {
            session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(
                            config.getProperty("SMTP_USERNAME"), config.getProperty("SMTP_PASSWORD"));
                }
            });
        } else {
            session = Session.getInstance(props);
        }

        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(sender));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(String.join(",", recipients)));
            message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(String.join(",", cc)));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
        } catch (AddressException e) {
            logger.severe("Invalid email address");
            throw new IllegalArgumentException("Invalid email address", e);
        } catch (MailConnectException | SendFailedException e) {
            // Possibly temporary error, throw checked exception
            logger.severe("Sending email failed (recoverable): " + e.getMessage());
            throw new EmailTransmissionFailedException("Sending email failed", e);
        } catch (MessagingException e) {
            // Non-recoverable error, throw unchecked exception
            logger.severe("Sending email failed: " + e.getMessage());
            throw new EmailServiceFailedException("Sending email failed", e);
        }
    }

}
