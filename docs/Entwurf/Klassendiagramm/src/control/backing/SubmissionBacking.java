package control.backing;

import java.util.HashMap;
import java.util.List;

import business.service.ReviewService;
import business.service.SubmissionService;
import dtos.Paper;
import dtos.Review;
import global.util.ResultListParameters;
import idk.SessionInformation;

public class SubmissionBacking {

	private SessionInformation sessionInformation;
	
	// Get these value directly from the view:
	
	private boolean allReviewsInput;
	
	private boolean notReleasedReviewsInput;
	
	private boolean releasedReviewsInput;
	
	private boolean allSubmittedReviewsInput;
	
	private boolean notSubmittedReviewsInput;
	
	private int versionNumberSelectInput;
	
	private int reviewerSelectInput;
	
	private boolean reviewerProposalSelectInput;
	
	private String searchTextInput;
	
	private HashMap<Integer, Boolean> recommendationPerPaper;
	
	private SubmissionService submissionService;
	
	private ReviewService reviewService;
	
	private ResultListParameters resultListParameters;
	
	private List<Paper> papers; 
	
	private List<Review> reviews;
	
	public void sortDateAsc() { }
	
	public void sortDateDesc() { }
	
	public void applyFilters() { }
	
	public void download(int paperId) { }
	
	public void releaseReview(int reviewId, int paperId) { }
	
	public void downloadPaper(int paperId) { }
	
	

}
