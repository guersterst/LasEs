package de.lases.control.backing;

import de.lases.business.service.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@RequestScoped
@Named
public class InitialConfigBacking implements Serializable {

    private CustomizationService customizationService;

    private String dbConnectionState;

    @PostConstruct
    public void init() {
    }

    public void createDb() {
    }

}
