package de.lases.business.service;

import de.lases.global.transport.ConnectionState;
import de.lases.global.transport.File;
import de.lases.global.transport.Style;
import de.lases.global.transport.SystemSettings;
import de.lases.persistence.repository.Transaction;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Event;
import jakarta.faces.component.UIMessage;
import jakarta.inject.Inject;

/**
 * Provides functionality for the customization of the applications look and feel and for
 * database information.
 * In case of an unexpected state, a {@link de.lases.global.transport.UIMessage} event will be fired.
 */
@Dependent
public class CustomizationService {

    @Inject
    private Event<UIMessage> uiMessageEvent;

    @Inject
    private Transaction transaction;


    /**
     * Sets the applications settings determining the look and feel.
     *
     * @param systemSettings The system's settings.
     */
    public void change(SystemSettings systemSettings) {
    }

    /**
     * @return The current settings of the system.
     */
    public SystemSettings get() {
        return null;
    }

    /**
     * Initiates the creation of the database schema.
     */
    public void createDbSchema() {
    }

    /**
     * @return The current state of the database connection.
     */
    public ConnectionState getConnectionState() {
        return null;
    }

    /**
     * Sets the applications' logo using the
     * {@link de.lases.business.util.AvatarUtil} utility to put it into
     * a fitting size.
     *
     * @param logo The applications logo.
     */
    public void setLogo(File logo) {
    }

    /**
     * @return The logo of the application.
     */
    public File getLogo() {
        return null;
    }
}
