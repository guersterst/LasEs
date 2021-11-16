package de.lases.business.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIMessage;
import jakarta.inject.Named;
import de.lases.global.transport.*;
import jakarta.enterprise.event.Event;
import de.lases.persistence.repository.*;

@ApplicationScoped
@Named
public class LoginService {

    private Event<UIMessage> uiMessageEvent;

    private Transaction transaction;

    public User login(String email, String password) {
        return null;
    }

    // TODO: @Joahnnes. Keine Ahnung ob man das braucht, ich gluabe wir haben
    // mal gesagt, dass wird nicht brauchen, aber schau nochmal
    public void logout() { }
}
