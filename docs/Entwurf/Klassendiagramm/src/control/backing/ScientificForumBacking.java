package control.backing;

import business.service.ScienceFieldService;
import business.service.ScientificForumService;
import business.service.SubmissionLieferando;
import dtos.ScientificForum;
import idk.SessionInformation;

public class ScientificForumBacking {

    private String searchString;

	private SessionInformation sessionInformation;
	
	private SubmissionLieferando submissionListService;
	
	private ScientificForumService scientificForumService;
	
	private ScientificForum scientificForum;
	
	private ScienceFieldService scienceFieldService;

    private List<Submission> submissions;
	
	public ScientificForum getScientificForum() { return null; }

    public void updateScientificForum(ScientificFourm scientificForum) { }

    public void setState(SubmissionState submissionState) { }

    // Hier resultlistparams übergeben.
    public void getSubmissions() { }

    public void deleteForum() { }

    public void addEditor() { }

    public void removeEditor() { }
}
