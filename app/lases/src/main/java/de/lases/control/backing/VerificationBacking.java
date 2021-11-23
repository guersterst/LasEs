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
     * Create the dtos for the verification.
     * No data will be loaded form the datasource at this point.
     */
    @PostConstruct
    public void init() {
    }

    /**
     * Read the verification random from the url and verify the user. On fail,
     * an error message will be displayed. On success, the user is now verified
     * and logged in.
     * This method is called by a view action when it can get the url parameter
     * from a view param.
     */
    public void onLoad() {

    }

    /**
     * Go to homepage if the user is now verified, go to welcome page if not.
     *
     * @return Go to next page.
     */
    public String goToHome() {
        return null;
    }

}
