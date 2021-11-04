package business.service;

import java.util.List;

import business.util.EmailService;
import dtos.Review;
import dtos.Submission;
import dtos.User;
import persistence.repository.ReviewRepository;

public class ReviewService {
	
	private ReviewRepository reviewRepository;
	
	private EmailService emailService;
	
	public void addReview(Submission submission, Review review) { }
	
	public void removeReview(Submission submission, int reviewId, int paperId) { }
	
	public List<Review> getReviews(Submission submission, int userId) { return null; }
	
	public void addReviewer(String email) { } // maybe user uebergeben?
	
	public void removeReviewer(User user) { }
	
	public List<User> getReviewers() { return null; }
	
	public void unlockReview() { }

}
