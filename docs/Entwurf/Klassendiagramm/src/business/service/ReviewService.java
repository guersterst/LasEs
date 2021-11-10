package business.service;

import java.util.List;

import business.util.EmailUtil;
import dtos.Review;
import dtos.Submission;
import dtos.User;
import persistence.repository.ReviewRepository;

@ApplicationScoped
public class ReviewService {

	private ExceptionQueue exceptionQueue;
	
	private EmailUtil emailUtil;

	private ReviewRepository reviewRepository;
	
	public void addReview(Submission submission, Review review) { }
	
	public void removeReview(Submission submission, int reviewId, int paperId) { }

	public List<Review> getReviews(Submission submission, User user) { return null; }

	public void uploadPDF(byte[] pdf) { }

	public byte[] downloadPDF() { return null; }
}
