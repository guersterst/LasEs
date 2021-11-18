package de.lases.business.service;

import de.lases.global.transport.*;
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
     * @param paper A {@code Paper} containing a valid id.
     */
    public Paper getPaper(Submission submission, Paper paper) {
        return null;
    }

    /**
     *
     * @param submission
     */
    public void requireRevision(Submission submission) {
    }

    public void unlockRevision(int paperId) {
    }

    /**
     * Adds a paper to a submission.
     * <p>
     * Whether this is a submission-pdf or a revision-pdf is determined internally.
     * </p>
     * @param file       The file to be added.
     * @param submission The submission, to which the file is being added.
     */
    public void addPaper(File file, Submission submission) {
    }

    /**
     * Returns a file.
     * @return The requested file.
     */
    //todo what param?
    public File downloadFile() {
        return null;
    }

    /**
     * Gets all papers associated with this submission.
     * <p>
     * The papers returned are determined by the highest privilege, which that
     * user possesses on that submission.
     * </p>
     * @param submission The submission for which the submissions are requested.
     * @param user  The user who requests the papers.
     * @return The list of papers of this submission the given user has access to.
     */
    public List<Paper> getPaperList(Submission submission, User user) {
        return null;
    }
}