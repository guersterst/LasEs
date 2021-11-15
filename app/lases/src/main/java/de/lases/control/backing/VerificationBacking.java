package de.lases.control.backing;

import de.lases.control.internal.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@RequestScoped
@Named
public class VerificationBacking implements Serializable { // y

    private SessionInformation sessionInformation;

    @PostConstruct
    public void init() {
    }

    public String goToHome() {
        return null;
    }

}
