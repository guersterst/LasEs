package de.lases.control.backing;

import de.lases.business.service.CustomizationService;
import de.lases.business.service.LoginService;
import de.lases.control.internal.SessionInformation;
import de.lases.global.transport.SystemSettings;
import de.lases.global.transport.User;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.annotation.FacesConfig;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**
 * Backing bean for the welcome page.
 */
@FacesConfig
@Named
@RequestScoped
public class WelcomeBacking {

    @Inject
    private SessionInformation sessionInformation;

    @Inject
    private LoginService loginService;

    @Inject
    private CustomizationService customizationService;

    private User loginInput;

    private SystemSettings systemSettings;

    /**
     * Load the system settings dto in order to get the  text displayed on the
     * welcome page and set up the user dto for login.
     */
    @PostConstruct
    public void init() {
        loginInput = new User();
        systemSettings = customizationService.get();
    }

    /**
     * Check the entered login data and either show an error message or go
     * to the homepage (and log the user in).
     *
     * @return Go to the homepage on success and nowhere on failure.
     */
    public String login() {
        User loginUser = loginService.login(loginInput);
        if (loginUser != null) {
            sessionInformation.setUser(loginUser);
            return "/views/authenticated/homepage?faces-redirect=true";
        }
        // UIMessage and stay on login page
        return null;
    }

    /**
     * Go to the register page.
     *
     * @return Go to the register page.
     */
    public String goToRegister() {
        return "/views/anonymous/register?faces-redirect=true";
    }

    /**
     * Get the user dto that holds the entered login information.
     *
     * @return The dto holding the entered login information.
     */
    public User getLoginInput() {
        return loginInput;
    }

    /**
     * Set the user dto that holds the entered login information.
     *
     * @param loginInput The dto holding the entered login information.
     */
    public void setLoginInput(User loginInput) {
        this.loginInput = loginInput;
    }

    /**
     * Get the system setting dto, which especially holds the headline and
     * the message for the welcome page.
     *
     * @return The system settings dto.
     */
    public SystemSettings getSystemSettings() {
        return systemSettings;
    }

    /**
     * Get session information.
     *
     * @return The session information.
     */
    public SessionInformation getSessionInformation() {
        return sessionInformation;
    }

}
