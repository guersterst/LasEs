package de.lases.business.service;

import de.lases.global.transport.*;
import de.lases.persistence.exception.NotFoundException;
import de.lases.persistence.repository.Transaction;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

import java.util.List;

/**
 * Provides functionality for handling papers in {@link Submission}s.
 * In case of an unexpected state, a {@link UIMessage} event will be fired.
 */
@Dependent
public class PaperService {

    @Inject
    private Event<UIMessage> uiMessageEvent;

    @Inject
    private Transaction transaction;

    /**
     * Gets a paper.
     * <p>
     * A {@link Paper} may be the paper associated with a submission or a revision.
     * </p>
     *
     * @param paper      A {@code Paper} containing a valid id of the requested paper.
     * @return A fully filled {@link Paper}.
     */
    public Paper get(Paper paper) {
        return null;
    }

    /**
     * Adds a paper to a submission.
     * <p>
     * Whether this is a submission-pdf or a revision-pdf is determined internally.
     * If this is a revision the editor will be informed by mail about this,
     * using the {@link de.lases.business.util.EmailUtil}-utility.
     * </p>
     *
     * @param file       The file to be added.
     * @param submission The submission, to which the file is being added.
     */
    //todo nur Paper übergeben
    public void add(FileDTO file, Paper paper) {
    }

    /**
     * Updates a paper.
     *
     * @param paper A {@link Paper}-DTO filled with the fields that are desired to be changed.
     *              <p>
     *              All fields filled with legal values will be overwritten, the rest are ignored.
     *              It should contain an existing id value.
     *              </p>
     */
    public void change(Paper paper) {

    }

    /**
     * Removes a paper from a submission.
     * <p>
     * The editor will be informed by mail about this,
     * using the {@link de.lases.business.util.EmailUtil}-utility.
     * </p>
     * @param paper A {@link Paper}-DTO containing a valid paper- and submission id.
     */
    public void remove(Paper paper) {

    }

    /**
     * Returns a requested file.
     *
     * @param paper The paper whose file is requested. Must contain a valid id.
     * @return The requested file.
     */
    public FileDTO getFile(Paper paper) {
        return null;
    }

    /**
     * Gets all papers associated with this submission.
     *
     * <p>
     * The papers returned are determined by the highest privilege, which that
     * user possesses on that submission.
     * </p>
     *
     * @param submission           The submission for which the submissions are requested. Must contain a valid id.
     * @param user                 The user who requests the papers, containing a valid view-privilege.
     * @param resultListParameters The parameters, that control filtering and sorting of the resulting list.
     * @return The list of papers of this submission the given user has access to.
     */
    public List<Paper> getList(Submission submission, User user,
                               ResultListParameters resultListParameters) {
        return null;
    }

    /**
     * Returns the latest paper of a submission.
     *
     * <p>
     * If there are no revisions for this submission the original {@link Paper} is the latest.
     * The paper returned is determined by the highest privilege, which that
     * user possesses on that submission. Meaning he might not have view access
     * on unreleased papers
     * </p>
     *
     * @param submission A {@link Submission}-DTO containing a valid id.
     * @param user       The user who requests the papers, containing a valid view-privilege.
     * @return The submissions paper, which was least recently uploaded.
     */
    public Paper getLatest(Submission submission, User user) {
        return null;
    }
}