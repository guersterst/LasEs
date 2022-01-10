package de.lases.business.service;

import de.lases.global.transport.*;
import de.lases.persistence.exception.DataNotWrittenException;
import de.lases.persistence.exception.InvalidFieldsException;
import de.lases.persistence.exception.NotFoundException;
import de.lases.persistence.repository.SystemSettingsRepository;
import de.lases.persistence.repository.Transaction;
import de.lases.persistence.util.DatasourceUtil;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

import java.io.IOException;
import java.util.PropertyResourceBundle;
import java.util.logging.Level;
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

    @Inject
    private PropertyResourceBundle resourceBundle;

    /**
     * Sets the application's settings, that determine its look and feel.
     *
     * @param systemSettings The filled {@link SystemSettings}.
     */
    public void change(SystemSettings systemSettings) {
        if (systemSettings.getImprint() == null || systemSettings.getCompanyName() == null
                || systemSettings.getHeadlineWelcomePage() == null || systemSettings.getMessageWelcomePage() == null
                || systemSettings.getStyle() == null) {
            logger.severe("One of the fields in systemSettings is null.");
            throw new InvalidFieldsException("Fields in systemSettings are null.");
        }

        Transaction transaction = new Transaction();

        try {
            SystemSettingsRepository.updateSettings(systemSettings, transaction);
            uiMessageEvent.fire(new UIMessage(resourceBundle.getString("successSystemsetting"),MessageCategory.INFO));
            logger.finest("Changed system settings");

            transaction.commit();
        }  catch (DataNotWrittenException exception) {

            uiMessageEvent.fire(new UIMessage(props.getString("dataNotWritten"), MessageCategory.ERROR));

            transaction.abort();
        }
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
     *
     * @return True if the creation succeeded.
     */
    public boolean createDataSourceSchema() {
        try {
            DatasourceUtil.createDatasource();
        } catch (IOException e) {
            logger.severe("Could not read SQL CREATE_ALL file. " + e.getMessage());
            uiMessageEvent.fire(new UIMessage("Could not read SQL CREATE_ALL file. " + e.getMessage(), MessageCategory.FATAL));
            return false;
        }
        return true;
    }

    /**
     * @return The current state of the database connection.
     */
    public ConnectionState getConnectionState() {
        return DatasourceUtil.testDatasourceConnection();
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
            throw new InvalidFieldsException(props.getString("idMissing"));
        }

        Transaction transaction = new Transaction();
        try {
            SystemSettingsRepository.setLogo(logo, transaction);
            uiMessageEvent.fire(new UIMessage(resourceBundle.getString("successSystemsetting"),MessageCategory.INFO));
            logger.finest("Successfully set the logo of the application.");

            transaction.commit();

        } catch (DataNotWrittenException ex) {
            transaction.abort();
            uiMessageEvent.fire(new UIMessage(props.getString("dataNotWritten"), MessageCategory.ERROR));
        }
    }

    /**
     * @return The logo of the application.
     */
    public FileDTO getLogo() {
        Transaction transaction = new Transaction();
        FileDTO logo;
        try {
            logo = SystemSettingsRepository.getLogo(transaction);
            transaction.commit();
        } catch (NotFoundException | DataNotWrittenException e) {
            transaction.abort();
            uiMessageEvent.fire(new UIMessage(props.getString("dataNotFound"), MessageCategory.ERROR));
            throw new IllegalStateException("No logo could be fetched.");
        }
        return logo;
    }
}
