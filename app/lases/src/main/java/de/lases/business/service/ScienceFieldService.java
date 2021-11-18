package de.lases.business.service;

import de.lases.global.transport.ScienceField;
import de.lases.global.transport.UIMessage;
import de.lases.persistence.repository.Transaction;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

import java.util.List;

/**
 * Provides functionality for handling the scientific fields ({@link ScienceField}s), that the application has.
 * In case of an unexpected state, a {@link UIMessage} event will be fired.
 */
@Dependent
public class ScienceFieldService {

    @Inject
    private Event<UIMessage> uiMessageEvent;

    @Inject
    private Transaction transaction;

    /**
     * Adds a scientific field to the database.
     *
     * @param field The scientific fields' data.
     */
    public void add(ScienceField field) {
    }

    /**
     * Determines whether a scientific field is already represented in the database.
     *
     * @param scienceField The data of the scientific field, which is to be evaluated.
     * @return {@code true} if this scientific field already exists, {@code false} otherwise.
     */
    public boolean exists(ScienceField scienceField) {
        return false;
    }

    /**
     * Removes a scientific field from the database.
     *
     * @param field The scientific field to be removed.
     */
    public void remove(ScienceField field) {
    }

    /**
     * Delivers all scientific fields.
     *
     * @return Returns all scientific fields.
     */
    public List<ScienceField> getScienceFields() {
        return null;
    }
}
