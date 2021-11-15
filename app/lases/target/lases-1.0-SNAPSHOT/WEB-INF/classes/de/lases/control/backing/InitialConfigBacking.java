package de.lases.control.backing;

import de.lases.business.service.CustomizationService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

@RequestScoped
@Named
public class InitialConfigBacking {

    private CustomizationService customizationService;

    private String dbConnectionState;

    @PostConstruct
    public void init() {
    }

    public void createDb() {
    }

}
