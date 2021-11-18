package de.lases.business.service;

import de.lases.global.transport.*;
import de.lases.persistence.exception.DataNotCompleteException;
import de.lases.persistence.exception.DatasourceQueryFailedException;
import de.lases.persistence.exception.InvalidQueryParamsException;
import de.lases.persistence.exception.NotFoundException;
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
     * Gets a list all science fields that belong to the specified scientific
     * forum.
     *
     * @param forum The {@code ScientificForum}-DTO with a valid id.
     * @param resultListParameters The parameters, that control filtering and sorting of the resulting list.
     * @return All scientific fields in which a given forum has expertise.
     */
    public static List<ScienceField> getList(ScientificForum forum,
                                             ResultListParameters
                                                     resultListParameters) {
        return null;
    }

    /**
     * Gets a list of all scientific fields that belong to a given user.
     *
     * @param user A {@code User}-DTO with a valid id.
     * @param resultListParameters The parameters, that control filtering and sorting of the resulting list.
     * @return All scientific fields in which a given user has expertise.
     */
    public static List<ScienceField> getList(User user, Transaction transaction,
                                             ResultListParameters
                                                     resultListParameters) {
        return null;
    }

    /**
     * Gets a list all science fields.
     *
     * @param resultListParameters The parameters, that control filtering and sorting of the resulting list.
     * @return All scientific fields.
     */
    public static List<ScienceField> getList(ResultListParameters resultListParameters) {
        return null;
    }
}
