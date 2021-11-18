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


    public void getSubmissions() {
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
}
