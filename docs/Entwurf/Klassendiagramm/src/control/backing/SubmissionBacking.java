package control.backing;

import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.Part;

import business.service.ReviewService;
import business.service.SubmissionService;
import business.service.UserService;
import control.internal.SessionInformation;
import dtos.Pagination;
import dtos.Paper;
import dtos.Review;
import dtos.Submission;
import dtos.SubmissionState;
import dtos.User;
import global.util.ResultListParameters;

@ViewScoped
public class SubmissionBacking {

	private SessionInformation sessionInformation;
	
	private UserService userService;
	
	private Submission submission;
	
	private Pagination<Paper> paperPagination;
	
	private Pagination<Review> reviewPagination;
	
	// Get these value directly from the view:
	
	private SubmissionService submissionService;
	
	private ReviewService reviewService;
	
	private Part uploadedRevisionPDF;
	
	private String forumName;
	
	private User author;
	
	private List<User> coAuthors;
	
	private DateSelect paper;
	
	private DateSelect review;
	
	private boolean visibleFilterInputPaper;
	private boolean visibleFilterInputReview;
	
	private boolean recommendationFilterInputReview;
	
	@PostConstruct
	public void init() { }
	
	public void setState(SubmissionState submissionState) { }
	
	public void downloadReview(int paperId) { }
	
	public void releaseReview(int reviewId, int paperId) { }
	
	public void downloadPaper(int paperId) { }
	
	public void uploadReview() { }
	
	public void acceptReviewing() { }
	
	public void declineReviewing() { }
	
	public void applyFilters() { }
	
	public void releaseRevision(int revisionId, int paperId) { }
	
	public void getReviewerForReview(Submission submission) { }
	
	public void getAuthorForPaper(Paper paper) { }
	
	public void uploadPDF() { }
	
	

}
