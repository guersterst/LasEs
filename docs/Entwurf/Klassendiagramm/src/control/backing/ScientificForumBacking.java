package control.backing;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;

import business.service.ScienceFieldService;
import business.service.ScientificForumService;
import business.service.SubmissionLieferando;
import control.internal.SessionInformation;
import dtos.ScientificForum;
import dtos.Submission;
import dtos.SubmissionState;

@RequestScoped
public class ScientificForumBacking {

    private String searchString;

	private SessionInformation sessionInformation;
	
	private SubmissionLieferando submissionListService;
	
	private ScientificForumService scientificForumService;
	
	private ScientificForum scientificForum;
	
	private ScienceFieldService scienceFieldService;

    private List<Submission> submissions;
    
    @PostConstruct
	public void init() { }
	
	public ScientificForum getScientificForum() { return null; }

    public void updateScientificForum(ScientificForum scientificForum) { }

    public void setState(SubmissionState submissionState) { }

    // Hier resultlistparams übergeben.
    public void getSubmissions() { }

    public void deleteForum() { }

    public void addEditor() { }

    public void removeEditor() { }
}
