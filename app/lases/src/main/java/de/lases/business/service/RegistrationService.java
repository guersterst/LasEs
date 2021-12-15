package de.lases.business.service;


import de.lases.business.internal.ConfigPropagator;
import de.lases.business.util.EmailUtil;
import de.lases.business.util.Hashing;
import de.lases.global.transport.MessageCategory;
import de.lases.global.transport.UIMessage;
import de.lases.global.transport.User;
import de.lases.global.transport.Verification;
import de.lases.persistence.exception.DataNotWrittenException;
import de.lases.persistence.exception.EmailTransmissionFailedException;
import de.lases.persistence.exception.NotFoundException;
import de.lases.persistence.repository.Transaction;
import de.lases.persistence.repository.UserRepository;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.PropertyResourceBundle;
import java.util.logging.Logger;

/**
 * Provides functionality for the registration and creation of users.
 * In case of an unexpected state, a {@link UIMessage} event will be fired.
 *
 * @author Thomas Kirz
 */
@Dependent
public class RegistrationService {

    private final Logger l = Logger.getLogger(RegistrationService.class.getName());

    @Inject
    private ConfigPropagator configPropagator;

    @Inject
    private PropertyResourceBundle message;

    @Inject
    private Event<UIMessage> uiMessageEvent;

    /**
     * Registers and creates a regular user in the database.
     * <p>
     * The email verification process is initiated using the
     * {@code EmailUtil} utility.
     * </p>
     *
     * @param user The user's data.
     *             <p>
     *             Must contain a valid email-address, that is not already in use.
     *             A password that will be hashed using the {@link de.lases.business.util.Hashing}-utility
     *             and a name and surname.
     *             </p>
     * @return The user with all their data, if successful, and {@code null} otherwise.
     */
    public User selfRegister(User user) {
        if (!userSufficientlyFilled(user)) {
            l.severe("User-DTO not sufficiently filled.");
            throw new IllegalArgumentException("User-DTO not sufficiently filled.");
        }

        Transaction t = new Transaction();

        // make sure that the user's email address is not already in use
        if (UserRepository.emailExists(user, t)) {
            l.warning("User with email address " + user.getEmailAddress() + " already exists.");
            uiMessageEvent.fire(new UIMessage(message.getString("emailInUse"),
                    MessageCategory.ERROR));
            t.abort();
            return null;
        }

        user.setRegistered(true);
        hashPassword(user);

        try {
            user = UserRepository.add(user, t);
        } catch (DataNotWrittenException e) {
            uiMessageEvent.fire(new UIMessage(message.getString("registrationFailed"), MessageCategory.ERROR));
            return null;
        }

        Verification verification = new Verification();
        verification.setVerified(false);
        verification.setUserId(user.getId());
        verification.setNonVerifiedEmailAddress(user.getEmailAddress());
        verification.setValidationRandom(Hashing.generateRandomSalt());
        verification.setTimestampValidationStarted(LocalDateTime.now());

        try {
            UserRepository.addVerification(verification, t);
            t.commit();
            l.fine("Verification for user " + user.getId() + " created.");
        } catch (NotFoundException | DataNotWrittenException e) {
            l.severe("Could not upload verification for user " + user.getId() + ".");
            uiMessageEvent.fire(new UIMessage(message.getString("registrationFailed"), MessageCategory.ERROR));
        }

        String emailBody = message.getString("email.verification.body.0")
                + user.getFirstName()
                + message.getString("email.verification.body.1")
                + generateValidationUrl(verification);

        try {
            EmailUtil.sendEmail(configPropagator.getProperty("MAIL_ADDRESS_FROM"), new String[]{user.getEmailAddress()},
                    null, message.getString("email.verification.subject"), emailBody);
        } catch (EmailTransmissionFailedException e) {
            uiMessageEvent.fire(new UIMessage(message.getString("registrationFailed"), MessageCategory.ERROR));
            return null;
        }

        // Success message, containing verification random if test mode is enabled
        String msg = message.getString("registrationSuccessful");
        if (configPropagator.getProperty("DEBUG_AND_TEST_MODE").equalsIgnoreCase("true")) {
            msg += "\n" + generateValidationUrl(verification);
        }
        uiMessageEvent.fire(new UIMessage(msg, MessageCategory.INFO));

        l.info("User " + user.getEmailAddress() + " registered.");

        return user;
    }

    private String generateValidationUrl(Verification verification) {
        return configPropagator.getProperty("BASE_URL") + "/views/anonymous/verification.xhtml?validationRandom="
                + verification.getValidationRandom();
    }

    /**
     * Registers and creates a user in the database.
     *
     * <p>
     * This user may be an admin.
     * Must contain a valid email-address, that is not already in use.
     * A password that will be hashed using the {@link de.lases.business.util.Hashing}-utility
     * and a name and surname.
     * </p>
     *
     * @param user The user's data.
     * @return The user with all their data, if successful, and {@code null} otherwise.
     */
    public User registerByAdmin(User user) {
        return null;
    }

    private boolean userSufficientlyFilled(User user) {
        return user.getEmailAddress() != null && !user.getEmailAddress().isEmpty()
                && user.getPasswordNotHashed() != null && !user.getPasswordNotHashed().isEmpty()
                && user.getFirstName() != null && !user.getFirstName().isEmpty()
                && user.getLastName() != null && !user.getLastName().isEmpty();
    }

    private void hashPassword(User user) {
        String salt = Hashing.generateRandomSalt();
        String hashedPassword = Hashing.hashWithGivenSalt(user.getPasswordNotHashed(), salt);
        user.setPasswordSalt(salt);
        user.setPasswordNotHashed(null);
        user.setPasswordHashed(hashedPassword);
    }
}
