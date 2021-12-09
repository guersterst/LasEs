package de.lases.business.service;

import de.lases.business.util.AvatarUtil;
import de.lases.global.transport.*;
import de.lases.persistence.exception.DataNotWrittenException;
import de.lases.persistence.exception.InvalidFieldsException;
import de.lases.persistence.repository.SystemSettingsRepository;
import de.lases.persistence.repository.Transaction;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

import java.io.IOException;
import java.util.PropertyResourceBundle;
import java.util.logging.Logger;

/**
 * Provides functionality for the customization of the applications look and feel and for
 * database information.
 * In case of an unexpected state, a {@link de.lases.global.transport.UIMessage} event will be fired.
 */
@Dependent
public class CustomizationService {

    @Inject
    private Event<UIMessage> uiMessageEvent;

    private static final Logger logger = Logger.getLogger(CustomizationService.class.getName());

    @Inject
    private PropertyResourceBundle props;

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
        return null;
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
        if  (logo == null || logo.getFile() == null) {
            logger.severe("The FileDTO or the image wrapped in it are null.");
            throw new InvalidFieldsException();
        }

        FileDTO thumbnailedLogo = new FileDTO();
        try {
            thumbnailedLogo = AvatarUtil.generateThumbnail(logo);
        } catch (IOException e) {
            uiMessageEvent.fire(new UIMessage(props.getString("imageNotThumbnailed"), MessageCategory.ERROR));
        }

        Transaction transaction = new Transaction();
        try {
            SystemSettingsRepository.setLogo(thumbnailedLogo, transaction);
            transaction.commit();
            logger.finest("Successfully set the logo of the application.");
        } catch (DataNotWrittenException ex) {
            transaction.abort();
            logger.severe("A DataNotWrittenException occurred when attempting to set the logo.");
            uiMessageEvent.fire(new UIMessage(props.getString("dataNotWritten"), MessageCategory.ERROR));
        }
    }

    /**
     * @return The logo of the application.
     */
    public FileDTO getLogo() {
        Transaction transaction = new Transaction();
        FileDTO logo = SystemSettingsRepository.getLogo(transaction);
        transaction.commit();
        return logo;
    }
}
