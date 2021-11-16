package de.lases.control.backing;

import de.lases.business.service.RegistrationService;
import de.lases.control.exception.IllegalAccessException;
import de.lases.control.internal.SessionInformation;
import de.lases.global.transport.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**
 * Backing bean for the new user page.
 */
@RequestScoped
@Named
public class NewUserBacking {

    @Inject
    private RegistrationService registrationService;

    @Inject
    SessionInformation sessionInformation;

    private User newUser;

    /**
     * Checks if the user accessing this site is really an administrator.
     *
     * @throws IllegalAccessException If the accessing user id not an
     *                                administrator.
     */
    @PostConstruct
    public void init() throws IllegalAccessException {

    }

    /**
     * Abort the creation of a new user and return to the user list.
     *
     * @return Go to the user list.
     */
    public String abort() {
        return null;
    }

    /**
     * Save the entered user and return to the user list.
     *
     * @return Go to the user list.
     */
    public String saveUser() {
        return null;
    }

    public User getNewUser() {
        return newUser;
    }

    /**
     * Set the {@code User} object that holds all information about the new user
     * the administrator has entered.
     *
     * @param newUser The new user that should be added.
     */
    public void setNewUser(User newUser) {
        this.newUser = newUser;
    }
}
