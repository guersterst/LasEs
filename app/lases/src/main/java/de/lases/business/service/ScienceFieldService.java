package de.lases.business.service;

import java.util.List;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Named;
import de.lases.persistence.repository.*;
import de.lases.global.transport.*;

@ApplicationScoped
@Named
public class ScienceFieldService {

    private Event<UIMessage> uiMessageEvent;

    private Transaction transaction;

    public void add(ScienceField field) {
    }

    public boolean exists(ScienceField scienceField) {
        return false;
    }

    public void remove(ScienceField field) {
    }

    public List<ScienceField> getScienceFields() {
        return null;
    }
}
