package control.backing;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ViewScoped;

import business.service.ProfileService;
import business.service.ScientificForumService;
import business.service.SubmissionLieferando;
import business.service.SubmissionService;
import business.service.UserService;
import control.internal.SessionInformation;
import dtos.LocalDate;
import dtos.Pagination;
import dtos.ScientificForum;
import dtos.Submission;
import dtos.SubmissionState;
import dtos.User;

@ViewScoped
public class ResultListBacking {
	
	private Pagination<Submission> submissionPagination;
	
	private Pagination<Submission> reviewedPagination;
	
	private Pagination<Submission> editedPagination;
	
	private Pagination<User> userPagination;
	
	private Pagination<ScientificForum> scientificForumPagination;
	
	private SubmissionService submissionListService;
	private ScientificForumService scientificForumService;
	private UserService userService;

	private SessionInformation sessionInformation;
	
	private SubmissionState stateFilterSelectSub;
	
	private SubmissionState stateFilterSelectEdit;
	
	private SubmissionState stateFilterSelectReview;
	
	private DateSelect submission;
	
	private DateSelect editorial;
	
	private DateSelect reviewing;
	
	private DateSelect forum;
	
	private LocalDate dateDeadlineLowerFilterSciForum;

	private LocalDate dateDeadlineUpperFilterSciFOrum;
	
	@PostConstruct
	public void init() {  }

}
