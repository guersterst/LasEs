package de.lases.control.backing;

import de.lases.business.service.*;
import de.lases.control.internal.*;
import de.lases.global.transport.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@RequestScoped
@Named
public class RegistrationBacking implements Serializable {

    private RegistrationService registrationService;

    private User newUser;

    private SessionInformation sessionInformation;

    @PostConstruct
    public void init() {
    }

    public String register() {
        return null;
    } // y

}
