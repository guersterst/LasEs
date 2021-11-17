package de.lases.business.service;

import jakarta.enterprise.context.Dependent;
import jakarta.faces.component.UIMessage;
import de.lases.global.transport.*;
import jakarta.enterprise.event.Event;
import de.lases.persistence.repository.*;
import jakarta.inject.Inject;

/**
 * Provides functionality regarding the authentication of a user.
 * In case of an unexpected state, a {@link de.lases.global.transport.UIMessage} event will be fired.
 */
@Dependent
public class LoginService {

    @Inject
    private Event<UIMessage> uiMessageEvent;

    @Inject
    private Transaction transaction;

    /**
     * Authenticates a user.
     *
     * @param user A {@link User}-DTO containing an email-adress and a non-hashed password.
     * @return If succesful the user with all their data, null otherwise.
     */
    public User login(User user) {
        return null;
    }
}
