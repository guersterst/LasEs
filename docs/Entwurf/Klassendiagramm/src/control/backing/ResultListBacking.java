package control.backing;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ViewScoped;

import business.service.ProfileService;
import business.service.ScientificForumService;
import business.service.SubmissionLieferando;
import control.internal.SessionInformation;
import dtos.Pagination;
import dtos.ScientificForum;
import dtos.Submission;
import dtos.User;

@ViewScoped
public class ResultListBacking {
	
	private Pagination<Submission> submissionPagination;
	
	private Pagination<Submission> reviewerPagination;
	
	private Pagination<Submission> editedPagination;
	
	private Pagination<User> userPagination;
	
	private Pagination<ScientificForum> scientificForumPagination;
	
	private SubmissionLieferando submissionListService;
	private ScientificForumService scientificForumService;
	private ProfileService profileService;

	private SessionInformation sessionInformation;
	
	@PostConstruct
	public void init() { }

}
