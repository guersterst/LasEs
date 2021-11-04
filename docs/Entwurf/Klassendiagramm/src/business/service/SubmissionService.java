package business.service;

import java.util.List;

import business.util.EmailService;
import dtos.Paper;
import dtos.Review;
import dtos.Submission;
import dtos.User;
import persistence.repository.SubmissionRepository;

public class SubmissionService {
	
	private EmailService emailUtil;
	
	private SubmissionRepository submissionRepository;
	
	public Submission getSubmission(int submissionId) { return null; }
	
	public void removeSubmission(Submission submission) { }
	
	public void addReview(Submission submission, Review review) { }
	
	public void removeReview(Submission submission, int reviewId, int paperId) { }
	
	public List<Review> getReviews(Submission submission, int userId) { return null; }
	
	public void addRevision(Submission submission, Paper revision) {} 
	
	public void addReviewer(String email) { } // maybe user uebergeben?
	
	public void removeReviewer(User user) { }
	
	public List<User> getReviewers() { return null; }
	
	public void addEditor(User user) { }
	
	public void removeEditor(User user) { }
	
	public List<User> getEditors() { return null; }
	
	public void unlockRevision(int paperId) { }

}
