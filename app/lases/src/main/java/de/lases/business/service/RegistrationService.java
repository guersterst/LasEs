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
        return null;
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

}
