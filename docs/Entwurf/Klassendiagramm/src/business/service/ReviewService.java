package business.service;

import java.util.List;

import business.util.EmailUtil;
import dtos.Review;
import dtos.Submission;
import dtos.User;
import persistence.repository.ReviewRepository;

public class ReviewService {

	private ExceptionQueue exceptionQueue;
	
	private EmailUtil emailUtil;

	private ReviewRepository reviewRepository;

	public Review getReview() { }
	
	public void addReview(Submission submission, Review review) { }

	public void changeReview() { }
	
	public void removeReview(Submission submission, int reviewId, int paperId) { }

	public List<Review> getReviews(Submission submission, int userId) { return null; }

	public void uploadPDF(byte[] pdf) { }

	public byte[] downloadPDF() { return null; }
}
