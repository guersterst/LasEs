package control.backing;


import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ViewScoped;

import business.service.SubmissionLieferando;
import business.service.SubmissionService;
import control.internal.SessionInformation;
import dtos.Pagination;
import dtos.Review;
import dtos.Submission;
import dtos.SubmissionState;
import global.util.ResultListFilter;
import global.util.ResultListParameters;

@ViewScoped
public class HomepageBacking {
	
	
	private enum Tab {
		OWN_SUBMISSIONS, SUBMISSIONS_TO_EDIT, SUBMISSIONS_TO_REVIEW
	}
	
	private Tab tab;
	
	private Pagination<Submission> submissionPagination;
	
	private Pagination<Submission> reviewerPagination;
	
	private Pagination<Submission> editedPagination;
	
	
	
	private ResultListParameters resultListParameters;

	private SessionInformation sessionInformation;
	
	private SubmissionService submissionListService;
	
	private SubmissionState stateFilterSelect;
	
	private LocalDateTime dateFilterUpperInput;
	
	private LocalDateTime dateFilterLowerInput;
	
	private String searchInput;
	
	private List<Submission> submissionList;
	
	private List<Review> reviewList;
	
	@PostConstruct
	public void init() { }
	
	public void showOwnSubmissionsTab() { }
	
	public void showSubmissionsToEditTab() { }
	
	public void showSubmissionsToReviewTab() { }
	
	public void sortDateAsc() { }
	
	public void sortDateDesc() { }
	
	public void applyFilters() { }

}
