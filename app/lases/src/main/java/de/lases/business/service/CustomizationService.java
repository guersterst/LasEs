package de.lases.business.service;

import de.lases.global.transport.ConnectionState;
import de.lases.global.transport.FileDTO;
import de.lases.global.transport.SystemSettings;
import de.lases.persistence.repository.SystemSettingsRepository;
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


    /**
     * Sets the application's settings, that determine its look and feel.
     *
     * @param systemSettings The filled {@link SystemSettings}.
     */
    public void change(SystemSettings systemSettings) {
    }

    /**
     * @return The current settings of the system, that determine its look and feel.
     */
    public SystemSettings get() {
        Transaction transaction = new Transaction();
        SystemSettings systemSettings = SystemSettingsRepository.getSettings(transaction);
        transaction.commit();
        return systemSettings;
    }

    /**
     * Initiates the creation of the datasource's schema.
     */
    public void createDataSourceSchema() {
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
    public void setLogo(FileDTO logo) {
    }

    /**
     * @return The logo of the application.
     */
    public FileDTO getLogo() {
        return null;
    }
}
