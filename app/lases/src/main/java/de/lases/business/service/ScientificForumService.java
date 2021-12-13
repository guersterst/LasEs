package de.lases.business.service;

import de.lases.global.transport.*;
import de.lases.persistence.exception.*;
import de.lases.persistence.repository.ScientificForumRepository;
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
 * Provides functionality regarding the handling of scientific forums.
 * In case of an unexpected state, a {@link UIMessage} event will be fired.
 *
 * @author Thomas Kirz
 * @author Johannes Garstenauer
 */
@Dependent
public class ScientificForumService implements Serializable {

    @Serial
    private static final long serialVersionUID = 5459943608069682810L;

    private static final Logger l = Logger.getLogger(SubmissionService.class.getName());

    @Inject
    private Event<UIMessage> uiMessageEvent;

    @Inject
    private PropertyResourceBundle message;

    /**
     * Gets a scientific forum.
     *
     * @param forum A {@link ScientificForum}-DTO containing a valid id.
     * @return The requested scientific forum.
     */
    public ScientificForum get(ScientificForum forum) {
        if (forum.getId() == null) {
            l.severe("ScientificForum id must not be null.");
            throw new IllegalArgumentException("ScientificForum id must not be null.");
        }

        ScientificForum result = null;
        Transaction t = new Transaction();

        try {
            result = ScientificForumRepository.get(forum, t);
            t.commit();
            l.finer("ScientificForum with id " + forum.getId() + " retrieved.");
        } catch (NotFoundException e) {
            l.severe("ScientificForum not found");
            uiMessageEvent.fire(new UIMessage(
                    message.getString("error.requestedScientificForumDoesNotExist"),
                    MessageCategory.ERROR));
            t.abort();
        }

        return result;
    }

    /**
     * Updates a scientific forum.
     *
     * @param newForum A {@link ScientificForum}
     *                 <p>
     *                 All fields filled with legal values will be overwritten.
     *                 It should contain an existing id and name.
     *                 </p>
     */
    public void change(ScientificForum newForum) {
        if (newForum.getId() == null || newForum.getName() == null) {

            l.severe("Must contain a name and forum id to change.");
            throw new InvalidFieldsException();
        }

        Transaction transaction = new Transaction();
        try {

            ScientificForumRepository.change(newForum, transaction);
            l.finest("Successfully changed the forum: " + newForum.getId() + ".");
        } catch (InvalidFieldsException | NotFoundException e) {

            l.severe(e.getMessage() + "caused the change operation to fail for: " + newForum.getId());
            uiMessageEvent.fire(new UIMessage(message.getString("dataNotFound"), MessageCategory.ERROR));
        } catch (DataNotWrittenException e) {

            l.severe("A database error occurred and the operation could not be performed.");
            uiMessageEvent.fire(new UIMessage(message.getString("dataNotWritten"), MessageCategory.ERROR));
        } catch (KeyExistsException e) {

            l.warning("It was attempted to create a forum with an existing name: " + newForum.getName());
            uiMessageEvent.fire(new UIMessage(message.getString("nameTaken"), MessageCategory.ERROR));
        } finally {
            transaction.commit();
        }
    }

    /**
     * Adds a scientific forum.
     * <p>
     * All editors will be informed about being added to this forum,
     * using the {@link de.lases.business.util.EmailUtil} utility.
     * </p>
     *
     * @param forum         The {@link ScientificForum}-DTO containing at least a name and
     *                      description.
     * @param scienceFields The scientific fields, which this forum is specialized in.
     * @param editors       The editors of this forum.
     */
    public void add(ScientificForum forum, List<ScienceField> scienceFields, List<User> editors) {
    }

    /**
     * Deletes a scientific forum and all associated submissions and reviews.
     *
     * @param forum The scientific forum to be deleted.
     */
    public void remove(ScientificForum forum) {
        if (forum.getId() == null) {

            l.severe("Must contain a forum id to remove");
            throw new InvalidFieldsException();
        }

        Transaction transaction = new Transaction();
        try {

            ScientificForumRepository.remove(forum, transaction);
            l.finest("Successfully removed the forum: " + forum.getId() + ".");
        } catch (NotFoundException e) {

            l.severe(e.getMessage() + "caused the removal operation to fail for: " + forum.getId());
            uiMessageEvent.fire(new UIMessage(message.getString("dataNotFound"), MessageCategory.ERROR));
        } catch (DataNotWrittenException e) {

            l.severe("A database error occurred and the operation could not be performed.");
            uiMessageEvent.fire(new UIMessage(message.getString("dataNotWritten"), MessageCategory.ERROR));
        } finally {
            transaction.commit();
        }
    }

    /**
     * Adds an editor to a scientific forum.
     *
     * @param editor The {@link User} added as an editor, containing a valid id.
     * @param forum  The {@link ScientificForum} to which the editor is being added,
     *               containing a valid id.
     */
    public void addEditor(User editor, ScientificForum forum) {
        if (forum.getId() == null || editor.getId() == null) {

            l.severe("Must contain a forum id and editor id to add in a relationship");
            throw new InvalidFieldsException();
        }

        Transaction transaction = new Transaction();
        try {

            ScientificForumRepository.addEditor(forum, editor, transaction);
            l.finest("Successfully added the forum: " + forum.getId() + " to the editor: "
                    + editor.getId());
        } catch (NotFoundException e) {

            l.severe(e.getMessage() + "\n Caused the operation to fail for: " + forum.getId());
            uiMessageEvent.fire(new UIMessage(message.getString("dataNotFound"), MessageCategory.ERROR));
        } catch (DataNotWrittenException e) {

            l.severe("A database error occurred and the operation could not be performed.");
            uiMessageEvent.fire(new UIMessage(message.getString("dataNotWritten"), MessageCategory.ERROR));
        } finally {
            transaction.commit();
        }
    }

    /**
     * Removes an editor from a scientific forum.
     *
     * @param editor The {@link User} removed as an editor, containing a valid id.
     * @param forum  The {@link ScientificForum} from which the editor is being removed,
     *               containing a valid id
     */
    public void removeEditor(User editor, ScientificForum forum) {
        if (forum.getId() == null || editor.getId() == null) {

            l.severe("Must contain a forum id and editor id to remove their relationship");
            throw new InvalidFieldsException();
        }

        Transaction transaction = new Transaction();
        try {

            ScientificForumRepository.removeEditor(forum, editor, transaction);
            l.finest("Successfully removed the editor: " + editor.getId() + " from the forum: "
                    + forum.getId());
        } catch (NotFoundException e) {

            l.severe(e.getMessage() + "\n Caused the operation to fail for: " + forum.getId());
            uiMessageEvent.fire(new UIMessage(message.getString("dataNotFound"), MessageCategory.ERROR));
        } catch (DataNotWrittenException e) {

            l.severe("A database error occurred and the operation could not be performed.");
            uiMessageEvent.fire(new UIMessage(message.getString("dataNotWritten"), MessageCategory.ERROR));
        } finally {
            transaction.commit();
        }
    }

    /**
     * Adds a scientific field to a forum in which it has expertise.
     *
     * @param scienceField The scientific field of expertise, with a valid id.
     * @param forum        The forum, which receives a new scientific field of expertise,
     *                     containing a valid id
     */
    public void addScienceField(ScienceField scienceField, ScientificForum forum) {
        if (forum.getId() == null || scienceField.getName() == null) {

            l.severe("Must contain a forum id and sciencefield name to enter in a relationship");
            throw new InvalidFieldsException();
        }

        Transaction transaction = new Transaction();
        try {

            ScientificForumRepository.addScienceField(forum, scienceField, transaction);
            l.finest("Successfully added the forum: " + forum.getId() + " to the topic: "
                    + scienceField.getName());
        } catch (NotFoundException e) {

            l.severe(e.getMessage() + " \n Caused the operation to fail for: " + forum.getId());
            uiMessageEvent.fire(new UIMessage(message.getString("dataNotFound"), MessageCategory.ERROR));
        } catch (DataNotWrittenException e) {

            l.severe("A database error occurred and the operation could not be performed.");
            uiMessageEvent.fire(new UIMessage(message.getString("dataNotWritten"), MessageCategory.ERROR));
        } finally {
            transaction.commit();
        }
    }

    /**
     * Removes an area of expertise from a scientific forum.
     *
     * @param scienceField    A {@link ScienceField} containing a valid id, which is removed
     *                        from the forum.
     * @param forum A {@link ScientificForum} from which the area of expertise e.g.
     *                        {@code ScienceField} is being removed from. Should contain
     *                        a valid id.
     */
    public void removeScienceField(ScienceField scienceField, ScientificForum forum) {
        if (forum.getId() == null || scienceField.getName() == null) {

            l.severe("Must contain a forum id and sciencefield name to enter in a relationship");
            throw new InvalidFieldsException();
        }

        Transaction transaction = new Transaction();
        try {

            ScientificForumRepository.removeScienceField(forum, scienceField, transaction);
            l.finest("Successfully removed the topic: " + scienceField.getName() + " from the forum: "
                    + forum.getId());
        } catch (NotFoundException e) {

            l.severe(e.getMessage() + "\n Caused the operation to fail for: " + forum.getId());
            uiMessageEvent.fire(new UIMessage(message.getString("dataNotFound"), MessageCategory.ERROR));
        } catch (DataNotWrittenException e) {

            l.severe("A database error occurred and the operation could not be performed.");
            uiMessageEvent.fire(new UIMessage(message.getString("dataNotWritten"), MessageCategory.ERROR));
        } finally {
            transaction.commit();
        }
    }

    /**
     * Gets all editors of a scientific forum.
     *
     * @param forum The {@link ScientificForum} where the editors are requested,
     *              containing a valid id.
     * @return All editors of the given forum.
     */
    public List<User> getEditors(ScientificForum forum) {
        return null;
    }


    /**
     * Determines whether a {@link ScientificForum} already exists.
     *
     * @param scientificForum A {@code ScientificForum} that must be filled
     *                        with a valid id or a name.
     * @return {@code true} if this {@link ScientificForum} exists and {@code false} otherwise.
     */
    public static boolean exists(ScientificForum scientificForum) {
        return false;
    }

    /**
     * Gets all scientific forums.
     *
     * @param resultListParams The parameters, that control filtering and sorting of the resulting list.
     * @return All scientific forums.
     */
    public List<ScientificForum> getList(ResultListParameters resultListParams) {
        return null;
    }
}
