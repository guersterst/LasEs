package de.lases.business.service;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.faces.component.UIMessage;
import jakarta.inject.Named;

import de.lases.persistence.repository.*;
import de.lases.global.transport.*;

/**
 * Provides functionality for the registration and creation of users.
 */
@Dependent
public class RegistrationService {

    private Event<UIMessage> uiMessageEvent;

    private Transaction transaction;

    /**
     * Registers and creates a regular user in the database.
     *
     * @param user The users data.
     */
    public void selfRegister(User user) {
        return null;
    }

    /**
     * Registers and creates a user in the database.
     * This user may be an admin.
     *
     * @param user The users data.
     */
    public void registerByAdmin(User user) {

    }

}
