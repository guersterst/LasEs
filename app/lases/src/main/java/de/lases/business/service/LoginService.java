package de.lases.business.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIMessage;
import jakarta.inject.Dependent;
import de.lases.global.transport.*;
import jakarta.enterprise.event.Event;
import de.lases.persistence.repository.*;

/**
 * Provides functionality regarding the authentication of a user.
 */
@Dependent
public class LoginService {

    private Event<UIMessage> uiMessageEvent;

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
