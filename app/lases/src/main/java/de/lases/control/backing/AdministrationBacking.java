package de.lases.control.backing;

import de.lases.business.service.CustomizationService;
import de.lases.global.transport.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.Part;

/**
 * Backing bean for the administration page.
 */
@RequestScoped
@Named
public class AdministrationBacking {

    @Inject
    private CustomizationService customizationService;

    private SystemSettings systemSettings;

    private Part uploadedLogo;

    /**
     * Loads the current system settings from the backend.
     */
    @PostConstruct
    public void init() {
    }

    /**
     * Validates the user input and shows specific error messages for each
     * invalid field or saves the changes with a confirmation message if none
     * are invalid.
     *
     * @return Show this page again.
     */
    public String save() {
        return null;
    }

    /**
     * Aborts all changes and returns the homepage.
     *
     * @return Show the homepage.
     */
    public String abort() {
        return null;
    }

    public SystemSettings getSystemSettings() {
        return systemSettings;
    }

    /**
     * Sets the system settings as saved in this bean.
     *
     * @param systemSettings New {@code SystemSettings} object.
     */
    public void setSystemSettings(SystemSettings systemSettings) {
        this.systemSettings = systemSettings;
    }

    public Part getUploadedLogo() {
        return uploadedLogo;
    }

    /**
     * Sets the logo as saved in this bean.
     *
     * @param uploadedLogo New {@code Part} object.
     */
    public void setUploadedLogo(Part uploadedLogo) {
        this.uploadedLogo = uploadedLogo;
    }

    /**
     * Return an array of all values the Style enum can have.
     *
     * @return ALl options of Style.
     */
    public Style[] getStyles() {
        return Style.values();
    }
}
