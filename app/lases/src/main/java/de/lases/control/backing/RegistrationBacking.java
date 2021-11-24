package de.lases.control.backing;

import de.lases.business.service.RegistrationService;
import de.lases.control.internal.*;
import de.lases.global.transport.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**
 * Backing bean for the registration page.
 */
@RequestScoped
@Named
public class RegistrationBacking {

    @Inject
    private RegistrationService registrationService;

    @Inject
    private SessionInformation sessionInformation;

    private User newUser;

    /**
     * Initializes the dto for the new user.
     */
    @PostConstruct
    public void init() {
    }

    /**
     * Register the new user and go to the homepage.
     *
     * @return Go to the homepage.
     */
    public String register() {
        return null;
    }

    /**
     * Get the dto of the new user.
     *
     * @return The new user.
     */
    public User getNewUser() {
        return newUser;
    }

    /**
     * Set the dto of the new user.
     *
     * @param newUser The new user.
     */
    public void setNewUser(User newUser) {
        this.newUser = newUser;
    }
}
