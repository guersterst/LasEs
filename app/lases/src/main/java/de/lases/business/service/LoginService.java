package de.lases.business.service;

import de.lases.persistence.exception.NotFoundException;
import jakarta.enterprise.context.Dependent;
import jakarta.faces.component.UIMessage;
import de.lases.global.transport.*;
import jakarta.enterprise.event.Event;
import de.lases.persistence.repository.*;
import jakarta.inject.Inject;

/**
 * Provides functionality regarding the authentication of a user.
 * In case of an unexpected state, a {@link UIMessage} event will be fired.
 */
@Dependent
public class LoginService {

    @Inject
    private Event<UIMessage> uiMessageEvent;

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
        try {
            User matchingEmailUser = UserRepository.get(user, transaction);
            // Hashing
            // if User.hash == matchingEmailUser.hash
            // return matchingEmailUser
            // else
            // uiMessage
            return null;
        } catch (NotFoundException e) {
            // uiMessage
            return null;
        }
    }
}