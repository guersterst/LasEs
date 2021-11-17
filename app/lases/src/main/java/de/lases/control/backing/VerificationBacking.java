package de.lases.control.backing;

import de.lases.business.service.UserService;
import de.lases.control.internal.*;
import de.lases.global.transport.Verification;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**
 * Backing bean for the verification page.
 */
@RequestScoped
@Named
public class VerificationBacking { // y

    @Inject
    private SessionInformation sessionInformation;

    @Inject
    private UserService userService;

    private Verification verification;

    /**
     * Read verification random from the url and verify the user. On fail, an
     * error message will be displayed. On success, the user is now verify and
     * logged in.
     */
    @PostConstruct
    public void init() {
    }

    /**
     * Go to homepage.
     *
     * @return Go to homepage.
     */
    public String goToHome() {
        return null;
    }

}
