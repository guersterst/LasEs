package control.backing;

import idk.SessionInformation;

import java.time.LocalDateTime;
import java.util.List;

import business.service.SubmissionLieferando;
import dtos.Review;
import dtos.Submission;
import dtos.SubmissionState;
import global.util.ResultListFilter;
import global.util.ResultListParameters;

public class HomepageBacking {
	
	
	private enum Tab {
		OWN_SUBMISSIONS, SUBMISSIONS_TO_EDIT, SUBMISSIONS_TO_REVIEW
	}
	
	private Tab tab;
	
	private ResultListParameters resultListParameters;

	private SessionInformation sessionInformation;
	
	private SubmissionLieferando submissionListService;
	
	private SubmissionState stateFilterSelect;
	
	private LocalDateTime dateFilterUpperInput;
	
	private LocalDateTime dateFilterLowerInput;
	
	private String searchInput;
	
	private List<Submission> submissionList;
	
	private List<Review> reviewList;
	
	public void showOwnSubmissionsTab() { }
	
	public void showSubmissionsToEditTab() { }
	
	public void showSubmissionsToReviewTab() { }
	
	public void sortDateAsc() { }
	
	public void sortDateDesc() { }
	
	public void applyFilters() { }

}
