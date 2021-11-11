package business.service;

import javax.faces.bean.ApplicationScoped;

@ApplicationScoped
public class RevisionService {

    public void getRevision() { }

    public void requireRevision(Submission submission) { }

    public void unlockRevision(int paperId) { }

    public void uploadFile(byte[] pdf) { }

    public Paper downloadFile() { return null; }

    public void getRevisions() { }
}