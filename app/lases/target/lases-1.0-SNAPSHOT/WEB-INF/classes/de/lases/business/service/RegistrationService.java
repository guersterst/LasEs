package de.lases.business.service;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.faces.component.UIMessage;
import jakarta.inject.Named;

import de.lases.persistence.repository.*;
import de.lases.global.transport.*;

@ApplicationScoped
@Named
public class RegistrationService {

    private Event<UIMessage> uiMessageEvent;

    private Transaction transaction;

    public User selfRegister(User user) {
        return null;
    }

    public void registerByAdmin(User user) {
    }

}
