package de.lases.control.backing;

import de.lases.business.service.UserService;
import de.lases.control.internal.*;
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

    /**
     * Verify the user that clicked the verification link.
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
