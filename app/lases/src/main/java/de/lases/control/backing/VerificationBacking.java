package de.lases.control.backing;

import de.lases.business.service.UserService;
import de.lases.control.exception.IllegalUserFlowException;
import de.lases.control.internal.*;
import de.lases.global.transport.Verification;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.event.ComponentSystemEvent;
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
     * Create the dto for the verification.
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
     * Checks if the view param is an integer and throws an exception if it is
     * not
     *
     * @param event The component system event that happens before rendering
     *              the view param.
     * @throws IllegalUserFlowException If there is no integer provided as view
     *                                  param
     */
    public void preRenderViewListener(ComponentSystemEvent event) {}

    /**
     * Go to homepage if the user is now verified, go to welcome page if not.
     *
     * @return Go to next page.
     */
    public String goToHome() {
        return null;
    }

    /**
     * Get the information about the verification of the user.
     *
     * @return The verification dto.
     */
    public Verification getVerification() {
        return verification;
    }

    /**
     * Set the information about the verification of the user.
     *
     * @param verification The verification dto.
     */
    public void setVerification(Verification verification) {
        this.verification = verification;
    }
}
