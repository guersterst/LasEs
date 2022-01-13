package de.lases.control.backing;

import de.lases.business.internal.ConfigPropagator;
import de.lases.business.service.CustomizationService;
import de.lases.control.exception.IllegalAccessException;
import de.lases.global.transport.FileDTO;
import de.lases.global.transport.MessageCategory;
import de.lases.global.transport.SystemSettings;
import de.lases.global.transport.UIMessage;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Backing bean for the administration page.
 *
 * @author Stefanie Gürster
 */
@RequestScoped
@Named
public class AdministrationBacking {

    @Inject
    private CustomizationService customizationService;

    @Inject
    private Event<UIMessage> uiMessageEvent;

    @Inject
    private transient PropertyResourceBundle resourceBundle;

     @Inject
     private ConfigPropagator configPropagator;

    private static final Logger logger = Logger.getLogger(AdministrationBacking.class.getName());

    private SystemSettings systemSettings;

    private Part uploadedLogo;

    private static final String STYLE_DIRECTORY = "design/css/themes/";

    /**
     * Loads the current system settings from the datasource.
     *
     * @throws IllegalAccessException If the accessing user is not an
     *                                administrator.
     */
    @PostConstruct
    public void init() {
        systemSettings = customizationService.get();
    }

    /**
     * Validates the user input and shows specific error messages for each
     * invalid field or saves the changes with a confirmation message if none
     * are invalid.
     *
     * @return Show this page again.
     */
    public String save() {
        customizationService.change(systemSettings);
        return "administration";
    }

    /**
     * Aborts all changes and returns the homepage.
     *
     * @return Show the homepage.
     */
    public String abort() {
        return "administration?faces-redirect=true";
    }

    /**
     * Gets the system settings as saved in this bean.
     *
     * @return The current system settings.
     */
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

    /**
     * Get the logo as saved in this bean.
     *
     * @return The uploaded logo.
     */
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
     * Upload a new logo.
     */
    public void uploadNewLogo() {

        try {
            FileDTO logo = new FileDTO();
            logo.setFile(uploadedLogo.getInputStream().readAllBytes());
            customizationService.setLogo(logo);
        } catch (IOException exception) {
            uiMessageEvent.fire(new UIMessage(resourceBundle.getString("uploadImage"), MessageCategory.WARNING));
            logger.severe("Upload a logo went wrong.");
        }
    }

    /**
     * Creates the path to the current stylesheet.
     *
     * @return Path to stylesheet.
     */
    public String getPathToStyle() {

        if (systemSettings == null) {
            // If the db connection failed, we just use the orange one.
            return STYLE_DIRECTORY + CustomizationService.DEFAULT_STYLE;
        } else {
            return STYLE_DIRECTORY.concat(systemSettings.getStyle());
        }

    }

    /**
     * Get all styles.
     *
     * @return all available styles.
     */
    public String[] getStyles() {
        return customizationService.loadStyles();
    }
}
