package de.lases.business.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Named;

import de.lases.persistence.repository.*;
import de.lases.global.transport.*;

@ApplicationScoped
@Named
public class RevisionService {

    private Event<UIMessage> uiMessageEvent;

    private Transaction transaction;

    public void getRevision() {
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