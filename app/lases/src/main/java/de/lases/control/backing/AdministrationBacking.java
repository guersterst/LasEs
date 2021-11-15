package de.lases.control.backing;

import de.lases.business.service.CustomizationService;
import de.lases.global.transport.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.Part;

@RequestScoped
@Named
public class AdministrationBacking {

    private CustomizationService customizationService;

    private SystemSettings systemSettings;

    private Part uploadedLogo;

    @PostConstruct
    public void init() {
    }

    public void save() {
    }

    public void abort() {
    }

}
