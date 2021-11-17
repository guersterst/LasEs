package de.lases.business.service;

import de.lases.global.transport.ConnectionState;
import de.lases.global.transport.Style;
import de.lases.global.transport.SystemSettings;
import de.lases.persistence.repository.Transaction;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Event;
import jakarta.faces.component.UIMessage;
import jakarta.inject.Inject;

/**
 * Provides functionality for the customization of the applications look and feel.
 * In case of an unexpected state, a {@link de.lases.global.transport.UIMessage} event will be fired.
 */
@Dependent
public class CustomizationService {

    @Inject
    private Event<UIMessage> uiMessageEvent;

    @Inject
    private Transaction transaction;

    //todo passen hier die methoden überhaupt zum bb
    /**
     *
     *
     * @param style
     * @param systemSettings
     */
    public void setSystemSettings(Style style, SystemSettings systemSettings) {
    }

    // TODO @Johannes: hier habe ich gemacht, dass das SystemSettings returned
    public SystemSettings get() {
        return null;
    }

    public void createDbSchema() {
    }

    // TODO @Johannes: returnd jetzt connectionState
    public ConnectionState getConnectionState() {
        return null;
    }

    public void setLogo() {
    }

    public void getLogo() {
    }
}
