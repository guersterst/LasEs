package de.lases.control.backing;

import de.lases.business.service.*;
import de.lases.global.transport.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.Part;

import java.io.Serializable;

@RequestScoped
@Named
public class AdministrationBacking implements Serializable {

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
