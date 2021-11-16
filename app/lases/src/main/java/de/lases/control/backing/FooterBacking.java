package de.lases.control.backing;

import de.lases.control.internal.SessionInformation;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**
 * Backing bean for the footer
 */
@Named
@RequestScoped
public class FooterBacking {

    @Inject
    private SessionInformation sessionInformation;

    public SessionInformation getSessionInformation() {
        return sessionInformation;
    }

    /**
     * Set the bean about the session information.
     *
     * @param sessionInformation
     */
    public void setSessionInformation(SessionInformation sessionInformation) {
        this.sessionInformation = sessionInformation;
    }

}
