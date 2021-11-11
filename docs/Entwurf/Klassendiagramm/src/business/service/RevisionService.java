package business.service;

@ApplicationScoped
public class RevisionService {

    private PaperRepository paperRepository;

    private SubmissionRepository submissionRepository;

    public void getRevision() { }

    public void requireRevision(Submission submission) { }

    public void unlockRevision(int paperId) { }

    public void uploadFile(byte[] pdf) { }

    public Paper downloadFile() { return null; }

    public void getRevisions() { }
}