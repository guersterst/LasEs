package business.service;

import javax.faces.bean.ApplicationScoped;
import javax.transaction.Transaction;
import javax.enterprise.event.Event;

@ApplicationScoped
public class RevisionService {

    private Event<UIMessage> uiMessageEvent;

    private Transaction transaction;

    public void getRevision() { }

    public void requireRevision(Submission submission) { }

    public void unlockRevision(int paperId) { }

    public void uploadFile(byte[] pdf) { }

    public Paper downloadFile() { return null; }

    public void getRevisions() { }
}