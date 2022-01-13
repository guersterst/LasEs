package de.lases.business.util;


import de.lases.business.internal.ConfigPropagator;
import de.lases.global.transport.ScientificForum;
import de.lases.global.transport.Submission;
import de.lases.persistence.exception.EmailTransmissionFailedException;
import de.lases.persistence.util.EmailSender;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.application.ProjectStage;
import jakarta.faces.context.FacesContext;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

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

        // In development or test mode, show a message to the user with a FacesMessage for each line
        FacesContext context = FacesContext.getCurrentInstance();
        if (!context.isProjectStage(ProjectStage.Production)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Email sent to " + String.join(", ", recipients), null));
            body.lines().forEach(line -> context.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_INFO, line, null)));
        }
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
        String mailto = "mailto:" + String.join(",", recipients);
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
     * Generates a URL for a lases page that can be shared via email.
     *
     * @param context The current faces context.
     * @param outcome The escaped path that the URL should point to, for example "views/authenticated/submission?id=3".
     * @return The URL that can be shared via email including scheme, host, port (if not standard), the context path and
     * the outcome.
     */
    public static String generateLinkForEmail(FacesContext context, String outcome) {
        // remove leading slash
        outcome = outcome.replaceAll("^/", "");

        String protocol = context.getExternalContext().getRequestScheme();
        String host = context.getExternalContext().getRequestServerName();
        String contextPath = context.getExternalContext().getRequestContextPath();
        int port = context.getExternalContext().getRequestServerPort();
        if (port != 80 && port != 443) {
            host += ":" + port;
        }

        return protocol + "://" + host + contextPath + "/" + outcome;
    }

    public static String generateSubmissionURL(Submission submission, FacesContext facesContext) {
        String base = generateLinkForEmail(facesContext, "views/authenticated/submission.xhtml");
        return facesContext.getExternalContext().encodeBookmarkableURL(base, Map.of("id", List.of(submission.getId().toString())));
    }

    public static String generateForumURL(ScientificForum scientificForum, FacesContext facesContext) {
        String base = generateLinkForEmail(facesContext, "views/authenticated/scientificForum.xhtml");
        return facesContext.getExternalContext().encodeBookmarkableURL(base, Map.of("id", List.of(scientificForum.getId().toString())));
    }

}
