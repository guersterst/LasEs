package de.lases.business.service;

import de.lases.global.transport.*;
import de.lases.persistence.exception.DataNotCompleteException;
import de.lases.persistence.exception.DataNotWrittenException;
import de.lases.persistence.exception.KeyExistsException;
import de.lases.persistence.repository.ScienceFieldRepository;
import de.lases.persistence.repository.Transaction;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.logging.Logger;

/**
 * Provides functionality for handling the scientific fields ({@link ScienceField}s), that the application has.
 * In case of an unexpected state, a {@link UIMessage} event will be fired.
 *
 * @author Sebastian Vogt
 */
@Dependent
public class    ScienceFieldService implements Serializable {

    @Serial
    private static final long serialVersionUID = 4604784260646118101L;

    @Inject
    private Event<UIMessage> uiMessageEvent;

    @Inject
    private PropertyResourceBundle resourceBundle;

    private static final Logger logger = Logger.getLogger(ScienceFieldService.class.getName());

    /**
     * Adds a {@link ScienceField} to the database.
     *
     * @param field All the scientific field's data.
     */
    public void add(ScienceField field) {
        Transaction transaction = new Transaction();

        try {
            ScienceFieldRepository.add(field, transaction);
            transaction.commit();
        } catch (DataNotWrittenException e) {
            transaction.abort();
            logger.warning(e.getMessage());
            uiMessageEvent.fire(new UIMessage(resourceBundle.getString("dataNotWritten"), MessageCategory.ERROR));
        } catch (KeyExistsException e) {
            transaction.abort();
            logger.warning(e.getMessage());
            uiMessageEvent.fire(new UIMessage(resourceBundle.getString("scienceFieldExists"), MessageCategory.ERROR));
        }
    }

    /**
     * Removes a {@link ScienceField} from the database.
     *
     * @param field The {@code ScienceField} to be removed.
     *              Must contain a valid id.
     */
    public void remove(ScienceField field) {
        Transaction transaction = new Transaction();

        try {
            ScienceFieldRepository.remove(field, transaction);
            transaction.commit();
        } catch (DataNotWrittenException e) {
            transaction.abort();
            logger.warning(e.getMessage());
            uiMessageEvent.fire(new UIMessage(resourceBundle.getString("dataNotRemoved"), MessageCategory.ERROR));
        }
    }

    /**
     * Gets a list all {@link ScienceField} that belong to the specified {@link ScientificForum}.
     *
     * @param forum                The {@code ScientificForum} whose fields of epertise are requested.
     *                             Must contain a valid id.
     * @param resultListParameters The parameters, that control filtering and sorting of the resulting list.
     * @return All scientific fields in which a given {@code ScientificForum} has expertise.
     */
    public List<ScienceField> getList(ScientificForum forum, ResultListParameters resultListParameters) {
        Transaction transaction = new Transaction();

        try {
            List<ScienceField> list = ScienceFieldRepository.getList(forum, transaction, resultListParameters);
            transaction.commit();
            return list;
        } catch (DataNotCompleteException e) {
            transaction.abort();
            logger.warning(e.getMessage());
            uiMessageEvent.fire(new UIMessage(resourceBundle.getString("dataNotComplete"),
                    MessageCategory.WARNING));
            return List.of();
        }
    }

    /**
     * Gets a list of all {@link ScienceField}s, in which a given {@link User} has expertise.
     *
     * @param user                 The {@code User}, whose {@code ScienceField}s are requested.
     *                             Must contain a valid id.
     * @param resultListParameters The parameters, that control filtering and sorting of the resulting list.
     * @return All scientific fields in which a given {@code User} has expertise.
     */
    public List<ScienceField> getList(User user, ResultListParameters resultListParameters) {
        Transaction transaction = new Transaction();

        try {
            List<ScienceField> list = ScienceFieldRepository.getList(user, transaction, resultListParameters);
            transaction.commit();
            return list;
        } catch (DataNotCompleteException e) {
            transaction.abort();
            logger.warning(e.getMessage());
            uiMessageEvent.fire(new UIMessage(resourceBundle.getString("dataNotComplete"),
                    MessageCategory.WARNING));
            return List.of();
        }
    }

    /**
     * Gets a list of all {@link ScienceField}s.
     *
     * @param resultListParameters The parameters, that control filtering and sorting of the resulting list.
     * @return All scientific fields.
     */
    public List<ScienceField> getList(ResultListParameters resultListParameters) {
        Transaction transaction = new Transaction();

        try {
            List<ScienceField> list = ScienceFieldRepository.getList(transaction, resultListParameters);
            transaction.commit();
            return list;
        } catch (DataNotCompleteException e) {
            transaction.abort();
            logger.warning(e.getMessage());
            uiMessageEvent.fire(new UIMessage(resourceBundle.getString("dataNotComplete"),
                    MessageCategory.WARNING));
            return List.of();
        }
    }

    /**
     * Checks if a given {@code ScienceField} already exists.
     *
     * @param scienceField The {@link ScienceField} to evaluate.
     *                     Must contain a name or an id.
     * @return {@code true} if this scientific field already exists, {@code false} otherwise.
     */
    public boolean exists(ScienceField scienceField) {
        Transaction transaction = new Transaction();

        boolean exists = ScienceFieldRepository.isScienceField(scienceField, transaction);
        transaction.commit();
        return exists;
    }
}
