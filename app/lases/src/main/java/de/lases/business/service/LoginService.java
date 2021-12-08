package de.lases.business.service;

import de.lases.business.util.Hashing;
import de.lases.global.transport.MessageCategory;
import de.lases.global.transport.UIMessage;
import de.lases.global.transport.User;
import de.lases.persistence.exception.NotFoundException;
import de.lases.persistence.repository.Transaction;
import de.lases.persistence.repository.UserRepository;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

import java.util.PropertyResourceBundle;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Provides functionality regarding the authentication of a user.
 * In case of an unexpected state, a {@link UIMessage} event will be fired.
 */
@Dependent
public class LoginService {

    @Inject
    private Event<UIMessage> uiMessageEvent;

    @Inject
    private PropertyResourceBundle propertyResourceBundle;

    /**
     * Authenticates a user
     * by comparing their identifier (e-mail address or id)
     * and hashed password with the database.
     *
     * @param user A {@link User}-DTO containing a valid email-address or id and password.
     * @return The user with all their data, if successful, and {@code null} otherwise.
     */
    public User login(User user) {
        Transaction transaction = new Transaction();
        Logger l = LogManager.getLogManager().getLogger(LoginService.class.getName());
        try {
            User matchingEmailUser = UserRepository.get(user, transaction);
            String expectedHash = matchingEmailUser.getPasswordHashed();
            String gotHash = Hashing.hashWithGivenSalt(user.getPasswordNotHashed(), matchingEmailUser.getPasswordSalt());
            if (gotHash.equals(expectedHash)) {
                // success
                l.info("Login successful for user " + user.getId() + " " + user.getEmailAddress());
                return matchingEmailUser;
            } else {
                // fail
                l.info("Login attempt unsuccessful for user " + user.getId() + " " + user.getEmailAddress());
                uiMessageEvent.fire(new UIMessage(propertyResourceBundle.getString("authFailed"), MessageCategory.ERROR));
            }
        } catch (NotFoundException e) {
            uiMessageEvent.fire(new UIMessage(propertyResourceBundle.getString("authFailed"), MessageCategory.FATAL));
        } finally {
            transaction.commit();
        }
        return null;
    }
}