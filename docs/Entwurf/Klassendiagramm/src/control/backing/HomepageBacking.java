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
	
	private Pagination<Submission> reviewedPagination;
	
	private Pagination<Submission> editedPagination;
	
	
	
	private ResultListParameters resultListParameters;

	private SessionInformation sessionInformation;
	
	private SubmissionService submissionListService;
	
	private SubmissionState stateFilterSelectSub;
	
	private SubmissionState stateFilterSelectEdit;
	
	private SubmissionState stateFilterSelectReview;
	
	private LocalDateTime dateFilterUpperInputSub;
	
	private LocalDateTime dateFilterUpperInputEdit;
	
	private LocalDateTime dateFilterUpperInputReview;
	
	private LocalDateTime dateFilterLowerInputSub;
	
	private LocalDateTime dateFilterLowerInputEdit;
	
	private LocalDateTime dateFilterLowerInputReview;
	
	private LocalDateTime dateFilterDeadlineUpperInputSub;
	
	private LocalDateTime dateFilterDeadlineUpperInputEdit;
	
	private LocalDateTime dateFilterDeadlineUpperInputReview;
	
	private LocalDateTime dateFilterDeadlineLowerInputSub;
	
	private LocalDateTime dateFilterDeadlineLowerInputEdit;
	
	private LocalDateTime dateFilterDeadlineLowerInputReview;
	
	private List<Submission> submissionList;
	
	private List<Submission> reviewedList;
	
	private List<Submission> editedList;
	
	@PostConstruct
	public void init() { }
	
	public void showOwnSubmissionsTab() { }
	
	public void showSubmissionsToEditTab() { }
	
	public void showSubmissionsToReviewTab() { }
	
	public void applyFilters() { }

}
