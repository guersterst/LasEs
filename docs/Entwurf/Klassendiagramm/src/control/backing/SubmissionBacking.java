package control.backing;

import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ViewScoped;

import business.service.ReviewService;
import business.service.SubmissionService;
import control.internal.SessionInformation;
import dtos.Pagination;
import dtos.Paper;
import dtos.Review;
import dtos.SubmissionState;
import global.util.ResultListParameters;

@ViewScoped
public class SubmissionBacking {

	private SessionInformation sessionInformation;
	
	private Pagination<Paper> paperPagination;
	
	private Pagination<Review> reviewPagination;
	
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
	
	@PostConstruct
	public void init() { }
	
	public void setState(SubmissionState submissionState) { }
	
	public void download(int paperId) { }
	
	public void releaseReview(int reviewId, int paperId) { }
	
	public void downloadPaper(int paperId) { }
	
	

}
