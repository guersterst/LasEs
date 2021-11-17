package de.lases.business.service;

import de.lases.global.transport.Paper;
import de.lases.global.transport.Submission;
import de.lases.global.transport.UIMessage;
import de.lases.persistence.repository.Transaction;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

/**
 * Provides functionality for the creation and manipulation of revisions for {@link Submission}s.
 * In case of an unexpected state, a {@link UIMessage} event will be fired.
 */
@Dependent
public class RevisionService {

    @Inject
    private Event<UIMessage> uiMessageEvent;

    @Inject
    private Transaction transaction;

    /**
     *
     * @param submission
     * @param version
     */
    public void getRevision(Submission submission, int version) {
    }

    public void requireRevision(Submission submission) {
    }

    public void unlockRevision(int paperId) {
    }

    public void uploadFile(byte[] pdf) {
    }

    public Paper downloadFile() {
        return null;
    }

    public void getRevisions() {
    }
}