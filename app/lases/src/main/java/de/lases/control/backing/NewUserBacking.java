package de.lases.control.backing;

import de.lases.business.service.*;
import de.lases.global.transport.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@RequestScoped
@Named
public class NewUserBacking implements Serializable {

    private RegistrationService registrationService;

    private User newUser;


    @PostConstruct
    public void init() {
    }

    public String abort() {
        return null;
    }

    public String saveUser() {
        return null;
    }
}
