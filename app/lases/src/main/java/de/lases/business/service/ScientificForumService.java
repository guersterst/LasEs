package de.lases.business.service;

import de.lases.global.transport.*;
import de.lases.persistence.exception.DatasourceQueryFailedException;
import de.lases.persistence.exception.InvalidFieldsException;
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
    public ScientificForum get(ScientificForum forum) {
        return null;
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
    }

    /**
     * Adds an editor to a scientific forum.
     *
     * @param editor The {@link User} added as an editor, containing a valid id.
     * @param forum  The {@link ScientificForum} to which the editor is being added,
     *               containing a valid id.
     */
    public void addEditor(User editor, ScientificForum forum) {
    }

    /**
     * Removes an editor from a scientific forum.
     *
     * @param editor The {@link User} removed as an editor, containing a valid id.
     * @param forum  The {@link ScientificForum} from which the editor is being removed,
     *               containing a valid id
     */
    public void removeEditor(User editor, ScientificForum forum) {
    }

    /**
     * Adds a scientific field to a forum in which it has expertise.
     *
     * @param scienceField The scientific field of expertise, with a valid id.
     * @param forum        The forum, which receives a new scientific field of expertise,
     *                     containing a valid id
     */
    public void addScienceField(ScienceField scienceField, ScientificForum forum) {
    }

    /**
     * Removes an area of expertise from a scientific forum.
     *
     * @param scienceField    A {@link ScienceField} containing a valid id, which is removed
     *                        from the forum.
     * @param scientificForum A {@link ScientificForum} from which the area of expertise e.g.
     *                        {@code ScienceField} is being removed from. Should contain
     *                        a valid id.
     */
    public void removeScienceField(ScienceField scienceField, ScientificForum scientificForum) {

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
     * Gets all scientific forums.
     *
     * @param resultListParams The parameters, that control filtering and sorting of the resulting list.
     * @return All scientific forums.
     */
    public List<ScientificForum> getList(ResultListParameters resultListParams) {
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
}
