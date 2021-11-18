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
 * Provides functionality regarding the handling of scientific forums.
 * In case of an unexpected state, a {@link UIMessage} event will be fired.
 */
@Dependent
public class ScientificForumService implements Serializable {

    @Serial
    private static final long serialVersionUID = 5459943608069682810L;

    @Inject
    private Event<UIMessage> uiMessageEvent;

    @Inject
    private Transaction transaction;

    /**
     * Gets a scientific forum.
     *
     * @param forum A {@link ScientificForum}-DTO containing a valid id.
     * @return The requested scientific forum.
     */
    public ScientificForum getForum(ScientificForum forum) {
        return null;
    }

    /**
     * Updates a scientific forum.
     *
     * @param newForum The new data of the scientific forum.
     *                 <p>
     *                 Should contain a valid id.
     *                 </p>
     */
    public void updateForum(ScientificForum newForum) {
    }

    /**
     * Adds a scientific forum.
     * <p>
     * All editors will be informed about being added to this forum,
     * using the {@link de.lases.business.util.EmailUtil} utility.
     * </p>
     *
     * @param forum         The {@link ScientificForum}-DTO containing all necessary data.
     * @param scienceFields The scientific fields, which this forum is specialized in.
     * @param editors       The editors of this forum.
     */
    public void addForum(ScientificForum forum, List<ScienceField> scienceFields, List<User> editors) {
    }

    /**
     * Deletes a scientific forum and all associated submissions and reviews.
     *
     * @param forum The scientific forum to be deleted.
     */
    public void removeForum(ScientificForum forum) {
    }

    /**
     * Adds an editor to a scientific forum.
     *
     * @param editor The user added as an editor.
     * @param forum  The forum to which the editor is being added.
     */
    public void addEditor(User editor, ScientificForum forum) {
    }

    /**
     * Removes an editor from a scientific forum.
     *
     * @param editor The user removed as an editor.
     * @param forum  The forum from which the editor is being removed.
     */
    public void removeEditor(User editor, ScientificForum forum) {
    }

    /**
     * Adds a scientific field to a forum in which it has expertise.
     *
     * @param scienceField The scientific field of expertise.
     * @param forum        The forum, which receives a new scientific field of expertise.
     */
    public void addScienceField(ScienceField scienceField, ScientificForum forum) {
    }

    /**
     * Gets all editors of a scientific forum.
     *
     * @param forum The forum where the editors are requested.
     * @return All editors of the given forum.
     */
    public List<User> getEditors(ScientificForum forum) {
        return null;
    }

    /**
     * Gets all scientific forums.
     *
     * @param resultListParams The parameters, that control filtering and sorting of the resulting list.
     * @return All scientific forums.
     */
    public List<ScientificForum> getForums(ResultListParameters resultListParams) {
        return null;
    }

    /**
     * Delivers all submissions, that a user either has submitted, is an editor to or reviews
     * in a specified scientific forum.
     *
     * @param scientificForum The {@link ScientificForum} where the wanted submissions are submitted to.
     * @param user            The user, whose editorial, reviewed or own submissions.
     * @param privilege       The role, to which submissions belong, in relation to a user.
     *                        Meaning, the user can request to receive the submissions which he is an editor to,
     *                        reviews or has submitted himself.
     * @param resultParams    Parameters, that control filtering and sorting of the resulting list.
     * @return The resulting list of submissions, that were submitted to a given scientific forum.
     */
    public List<Submission> getSubmissions(ScientificForum scientificForum, User user, Privilege privilege,
                                           ResultListParameters resultParams) {
        return null;
    }
}
