package business.service;

import java.util.List;
import javax.transaction.Transaction;
import javax.enterprise.event.Event;
import javax.faces.bean.ApplicationScoped;

import business.util.EmailUtil;
import dtos.Review;
import dtos.Submission;
import dtos.User;
import persistence.repository.ReviewRepository;

@ApplicationScoped
public class ReviewService {

	private Event<UIMessage> uiMessageEvent;

	private Transaction transaction;

	public void addReview(Submission submission, Review review) { }
	
	public void removeReview(Submission submission, int reviewId, int paperId) { }

	public List<Review> getReviews(Submission submission, User user) { return null; }

	public void uploadFile(byte[] pdf) { }

	public byte[] downloadFile() { return null; }
}
