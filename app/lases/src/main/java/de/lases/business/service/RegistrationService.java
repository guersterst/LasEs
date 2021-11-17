package de.lases.business.service;


import de.lases.global.transport.User;
import de.lases.persistence.repository.Transaction;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Event;
import jakarta.faces.component.UIMessage;
import jakarta.inject.Inject;

/**
 * Provides functionality for the registration and creation of users.
 * In case of an unexpected state, a {@link UIMessage} event will be fired.
 */
@Dependent
public class RegistrationService {

    @Inject
    private Event<UIMessage> uiMessageEvent;

    @Inject
    private Transaction transaction;

    /**
     * Registers and creates a regular user in the database.
     *
     * @param user The user's data.
     */
    public void selfRegister(User user) {

    }

    /**
     * Registers and creates a user in the database.
     * This user may be an admin.
     *
     * @param user The user's data.
     */
    public void registerByAdmin(User user) {

    }

}
