package de.lases.business.service;

import de.lases.global.transport.*;
import de.lases.persistence.repository.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.faces.component.UIMessage;
import jakarta.inject.Named;

@ApplicationScoped
@Named
public class CustomizationService {

    private Event<UIMessage> uiMessageEvent;

    private Transaction transaction;

    public void setSystemSettings(Style style) {
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
