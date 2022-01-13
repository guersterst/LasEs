package de.lases.business.service;


import de.lases.business.util.EmailUtil;
import de.lases.business.util.Hashing;
import de.lases.global.transport.MessageCategory;
import de.lases.global.transport.UIMessage;
import de.lases.global.transport.User;
import de.lases.global.transport.Verification;
import de.lases.persistence.exception.DataNotWrittenException;
import de.lases.persistence.exception.EmailTransmissionFailedException;
import de.lases.persistence.exception.KeyExistsException;
import de.lases.persistence.exception.NotFoundException;
import de.lases.persistence.repository.Transaction;
import de.lases.persistence.repository.UserRepository;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Event;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
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

    private final Logger logger = Logger.getLogger(RegistrationService.class.getName());

    @Inject
    private PropertyResourceBundle message;

    @Inject
    private Event<UIMessage> uiMessageEvent;

    @Inject
    private FacesContext facesContext;

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
     * @author Thomas Kirz
     */
    public User selfRegister(User user) {
        if (!userSufficientlyFilled(user)) {
            logger.severe("User-DTO not sufficiently filled.");
            throw new IllegalArgumentException("User-DTO not sufficiently filled.");
        }

        Transaction t = new Transaction();

        user = register(user, t);

        if (user != null && initiateVerificationProcess(user, t)) {
            String msg = message.getString("registrationSuccessful");
            uiMessageEvent.fire(new UIMessage(msg, MessageCategory.INFO));

            logger.info("User " + user.getEmailAddress() + " registered.");
            t.commit();
        } else {
            uiMessageEvent.fire(new UIMessage(message.getString("registrationFailed"), MessageCategory.ERROR));
            t.abort();
        }

        return user;
    }

    private User register(User user, Transaction t) {
        user.setRegistered(true);
        hashPassword(user);

        // check if a user with that email address already exists
        if (UserRepository.emailExists(user, t)) {
            User oldUser;
            try {
                oldUser = UserRepository.get(user, t);
            } catch (NotFoundException e) {
                t.abort();
                return null;
            }
            if (oldUser.isRegistered()) {
                logger.fine("User with email address " + user.getEmailAddress() + " is already registered.");
                uiMessageEvent.fire(new UIMessage(message.getString("emailInUse"),
                        MessageCategory.ERROR));
                t.abort();
                return null;
            }
            user.setId(oldUser.getId());
            try {
                UserRepository.change(user, t);
            } catch (DataNotWrittenException | NotFoundException | KeyExistsException e) {
                t.abort();
                return null;
            }
        } else {
            try {
                UserRepository.add(user, t);
            } catch (DataNotWrittenException e) {
                t.abort();
                return null;
            }
        }

        return user;
    }

    /**
     * Creates a verification dto with a random string and sends an email to the user.
     * If the development or test project stage is enabled, the random string will be shown as a UIMessage.
     * No other messages will be shown.
     *
     * @param user The user to be verified filled with id and email address.
     * @return If the verification process was successfully initiated.
     */
    private boolean initiateVerificationProcess(User user, Transaction t) {

        Verification verification = new Verification();
        verification.setVerified(false);
        verification.setUserId(user.getId());
        verification.setNonVerifiedEmailAddress(user.getEmailAddress());
        verification.setValidationRandom(Hashing.generateRandomSalt());
        verification.setTimestampValidationStarted(LocalDateTime.now(ZoneOffset.UTC));

        try {
            UserRepository.addVerification(verification, t);
            logger.fine("Verification for user " + user.getId() + " created.");
        } catch (NotFoundException | DataNotWrittenException e) {
            return false;
        }

        String emailBody = message.getString("email.verification.body.0")
                + user.getFirstName()
                + message.getString("email.verification.body.1")
                + generateValidationUrl(verification);

        try {
            EmailUtil.sendEmail(new String[]{user.getEmailAddress()},
                    null, message.getString("email.verification.subject"), emailBody);
        } catch (EmailTransmissionFailedException e) {
            return false;
        }

        return true;
    }

    private String generateValidationUrl(Verification verification) {
        String base = EmailUtil.generateLinkForEmail(facesContext, "views/anonymous/verification.xhtml");
        return facesContext.getExternalContext().encodeBookmarkableURL(base,
                Map.of("validationRandom", List.of(verification.getValidationRandom())));
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
        if (!userSufficientlyFilled(user)) {
            logger.severe("User-DTO not sufficiently filled.");
            throw new IllegalArgumentException("User-DTO not sufficiently filled.");
        }

        Transaction t = new Transaction();
        user = register(user, t);

        if (user == null) {
            t.abort();
            uiMessageEvent.fire(new UIMessage(message.getString("registrationFailed"), MessageCategory.ERROR));
            return null;
        }

        Verification verification = new Verification();
        verification.setVerified(true);
        verification.setUserId(user.getId());
        try {
            UserRepository.addVerification(verification, t);
        } catch (NotFoundException | DataNotWrittenException e) {
            uiMessageEvent.fire(new UIMessage(message.getString("registrationFailed"), MessageCategory.ERROR));
            t.abort();
            return null;
        }

        uiMessageEvent.fire(new UIMessage(message.getString("registrationSuccessful"), MessageCategory.INFO));
        t.commit();
        return user;
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
