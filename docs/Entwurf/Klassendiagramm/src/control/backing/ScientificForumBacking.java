package control.backing;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ViewScoped;

import business.service.ScienceFieldService;
import business.service.ScientificForumService;
import business.service.SubmissionLieferando;
import business.service.SubmissionService;
import control.internal.SessionInformation;
import dtos.Pagination;
import dtos.ScienceField;
import dtos.ScientificForum;
import dtos.Submission;
import dtos.SubmissionState;
import dtos.User;

@ViewScoped
public class ScientificForumBacking {

    private String newEditorInput;
    
    private Pagination<Submission> submissionPagination;
	
	private Pagination<Submission> reviewerPagination;
	
	private Pagination<Submission> editedPagination;

	private SessionInformation sessionInformation;
	
	private SubmissionService submissionListService;
	
	private ScientificForumService scientificForumService;
	
	private ScientificForum scientificForum;
	
	private ScienceFieldService scienceFieldService;

    private List<Submission> submissions;
    
    private List<User> editors;
    
    private List<ScienceField> scieneFields;
    
    @PostConstruct
	public void init() { }
	
	public ScientificForum getScientificForum() { return null; }

    public void updateScientificForum(ScientificForum scientificForum) { }

    // Hier resultlistparams übergeben.
    public void getSubmissions() { }

    public void deleteForum() { }

    public void addEditor() { }

    public void removeEditor() { }
  
}
