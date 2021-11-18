package de.lases.business.service;

import de.lases.global.transport.*;
import de.lases.persistence.repository.Transaction;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

import java.util.List;

/**
 * Provides functionality regarding the handling of scientific forums.
 * In case of an unexpected state, a {@link UIMessage} event will be fired.
 */
@Dependent
public class ScientificForumService {

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
     * <p>
     * All editors will be informed about being added to this forum,
     * using the {@link de.lases.business.util.EmailUtil} utility.
     * </p>
     *
     * @param editor
     */
    public void addEditor(User editor) {
    }

    public void removeEditor(User user) {
    }

    public void addScienceField() {
    }

    public List<User> getEditors() {
        return null;
    }

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
