package de.lases.control.backing;

import de.lases.business.service.RegistrationService;
import de.lases.global.transport.User;
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

    private User newUser;

    /**
     * Initializes the dto for the new user.
     */
    @PostConstruct
    public void init() {
        newUser = new User();
    }

    /**
     * Register the new user, send the email with the email verification link
     * to the address the user has specified and go to the welcome page, where
     * a message is shown that asks the user to check his inbox for the
     * verification email.
     *
     * @return Go to the welcome page.
     */
    public String register() {
        if (registrationService.selfRegister(newUser) != null) {
            return "/views/anonymous/welcome";
        } else {
            return null;
        }
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
