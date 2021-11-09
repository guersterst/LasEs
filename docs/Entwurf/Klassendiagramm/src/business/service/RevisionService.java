package business.service;

@ApplicationScoped
public class RevisionService {

    private ExceptionQueue exceptionQueue;

    private PaperRepository paperRepository;

    private SubmissionRepository submissionRepository;

    public void getRevision() { }

    public void addRevision(Paper revision) { }

    public void unlockRevision(int paperId) { }

    public void uploadPaper(byte[] pdf) { }

    public Paper downloadPaper() { return null; }

    public void getRevisions() { }
}