package de.lases.business.service;

import de.lases.global.transport.*;
import de.lases.persistence.exception.DatasourceQueryFailedException;
import de.lases.persistence.exception.NotFoundException;
import de.lases.persistence.repository.Transaction;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

import java.io.IOException;
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
     * <p></p>
     * A {@link Paper} may be the paper associated with a submission or a revision.
     *
     * @param submission The submission of the paper.
     * @param paper      A {@code Paper} containing a valid id.
     */
    public Paper getPaper(Submission submission, Paper paper) {
        return null;
    }

    /**
     * Adds the requirement of a new revision to a given submission.
     *
     * @param submission The submission for which a revision is requested.
     */
    public void requireRevision(Submission submission) {
    }

    /**
     * Unlocks a revision.
     *
     * @param paper The revision to be unlocked
     */
    public void unlockRevision(Paper paper) {
    }

    /**
     * Adds a paper to a submission.
     * <p>
     * Whether this is a submission-pdf or a revision-pdf is determined internally.
     * </p>
     *
     * @param file       The file to be added.
     * @param submission The submission, to which the file is being added.
     */
    public void addPaper(File file, Submission submission) {
    }

    /**
     * Returns a requested file.
     *
     * @param paper The paper whose file is requested.
     * @return The requested file.
     */
    public File downloadFile(Paper paper) {
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
     * @param submission           The submission for which the submissions are requested.
     * @param user                 The user who requests the papers.
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
     * The paper returned is determined by the highest privilege, which that
     * user possesses on that submission. Meaning he might not have view access
     * on unreleased papers
     * </p>
     *
     * @param submission A {@link Submission}-DTO containing a valid id.
     * @param user       The user who requests the papers.
     * @return The latest paper.
     */
    public Paper getLatest(Submission submission, User user)
            throws NotFoundException {
        return null;
    }
}