package de.lases.business.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIMessage;
import jakarta.inject.Dependent;
import de.lases.global.transport.*;
import jakarta.enterprise.event.Event;
import de.lases.persistence.repository.*;

/**
 * Provides functionality regarding the authentication and loginof a user..
 */
@Dependent
public class LoginService {

    private Event<UIMessage> uiMessageEvent;

    private Transaction transaction;

    public User login(String email, String password) {
        return null;
    }
}
