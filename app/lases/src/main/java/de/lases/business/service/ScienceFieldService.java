package de.lases.business.service;

import de.lases.global.transport.*;
import de.lases.persistence.repository.Transaction;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Provides functionality for handling the scientific fields ({@link ScienceField}s), that the application has.
 * In case of an unexpected state, a {@link UIMessage} event will be fired.
 */
@Dependent
public class ScienceFieldService implements Serializable {

    @Serial
    private static final long serialVersionUID = 4604784260646118101L;

    @Inject
    private Event<UIMessage> uiMessageEvent;

    @Inject
    private Transaction transaction;

    /**
     * Gets a {@link ScienceField}.
     *
     * @param name A {@code String} name of a scienceField.
     * @return The fully filled requested {@code ScienceField} or null if it doesn't exist.
     */
    public ScienceField get(String name) {
        return null;
    }

    /**
     * Adds a {@link ScienceField} to the database.
     *
     * @param field All the scientific field's data.
     */
    public void add(ScienceField field) {
    }

    /**
     * Removes a {@link ScienceField} from the database.
     *
     * @param field The {@code ScienceField} to be removed.
     *              Must contain a valid id.
     */
    public void remove(ScienceField field) {
    }

    /**
     * Gets a list all {@link ScienceField} that belong to the specified {@link ScientificForum}.
     *
     * @param forum                The {@code ScientificForum} whose fields of epertise are requested.
     *                             Must contain a valid id.
     * @param resultListParameters The parameters, that control filtering and sorting of the resulting list.
     * @return All scientific fields in which a given {@code ScientificForum} has expertise.
     */
    public static List<ScienceField> getList(ScientificForum forum, ResultListParameters resultListParameters) {
        return null;
    }

    /**
     * Gets a list of all {@link ScienceField}s, in which a given {@link User} has expertise.
     *
     * @param user                 The {@code User}, whose {@code ScienceField}s are requested.
     *                             Must contain a valid id.
     * @param resultListParameters The parameters, that control filtering and sorting of the resulting list.
     * @return All scientific fields in which a given {@code User} has expertise.
     */
    public static List<ScienceField> getList(User user, ResultListParameters resultListParameters) {
        return null;
    }

    /**
     * Gets a list of all {@link ScienceField}s.
     *
     * @param resultListParameters The parameters, that control filtering and sorting of the resulting list.
     * @return All scientific fields.
     */
    public static List<ScienceField> getList(ResultListParameters resultListParameters) {
        return null;
    }

    /**
     * Checks if a given {@code ScienceField} already exists.
     *
     * @param scienceField The {@link ScienceField} to evaluate.
     *                     Must contain a name or an id.
     * @return {@code true} if this scientific field already exists, {@code false} otherwise.
     */
    public boolean exists(ScienceField scienceField) {
        return false;
    }
}
