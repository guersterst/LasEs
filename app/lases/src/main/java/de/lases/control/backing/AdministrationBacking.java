package de.lases.control.backing;

import de.lases.business.service.CustomizationService;
import de.lases.control.exception.IllegalAccessException;
import de.lases.global.transport.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Event;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.util.PropertyResourceBundle;
import java.util.logging.Logger;

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

    private static final Logger logger = Logger.getLogger(AdministrationBacking.class.getName());

    private SystemSettings systemSettings;

    private Part uploadedLogo;

    private static final String PATH_TO_STYLE_DIRECTORY = "design/css/themes/";

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
        uiMessageEvent.fire(new UIMessage(resourceBundle.getString("successSystemsetting"),MessageCategory.INFO));
        return "administration?faces-redirect=true";
    }

    /**
     * Aborts all changes and returns the homepage.
     *
     * @return Show the homepage.
     */
    public String abort() {
        return null;
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

            logger.finest("Upload of a new logo was successful.");
        } catch (IOException exception) {
            uiMessageEvent.fire(new UIMessage(resourceBundle.getString("uploadImage"), MessageCategory.WARNING));
            logger.severe("Upload a logo went wrong.");
        }
    }

    /**
     *
     * @return
     */
    public String getPathToStyle() {

        InputStream inputStream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/" +  PATH_TO_STYLE_DIRECTORY);

        return null;
    }

    public String[] getStyles() {
        return null;
    }
}
