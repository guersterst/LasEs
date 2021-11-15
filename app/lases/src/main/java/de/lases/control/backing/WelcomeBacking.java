package de.lases.control.backing;

import de.lases.business.service.CustomizationService;
import de.lases.business.service.LoginService;
import de.lases.control.internal.*;
import de.lases.global.transport.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.annotation.FacesConfig;
import jakarta.inject.Named;

import java.io.Serializable;

/**
 * This is the backing bean for welcome.xhtml. On that page, registered users can log in.
 */
@FacesConfig
@Named
@RequestScoped
public class WelcomeBacking {

    private LoginService loginService;

    private String emailInput; // y

    private String passwordInput; // y

    private SystemSettings systemSettings;

    private CustomizationService customizationService;

    private SessionInformation sessionInformation;

    @PostConstruct
    public void init() {
    }

    public String login() {
        return "welcome";
    } // y

    public String goToRegister() {
        return "register";
    }

    public String getEmailInput() {
        return emailInput;
    }

    public void setEmailInput(String emailInput) {
        this.emailInput = emailInput;
    }

    public String getPasswordInput() {
        return passwordInput;
    }

    public void setPasswordInput(String passwordInput) {
        this.passwordInput = passwordInput;
    }
}
