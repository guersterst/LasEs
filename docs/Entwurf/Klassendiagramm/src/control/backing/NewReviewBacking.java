package control.backing;

import javax.annotation.PostConstruct;

import business.internal.SessionInformation;
import business.service.ReviewService;


public class NewReviewBacking {
	
	private ReviewService reviewService;

	private SessionInformation sessionInformation;

	boolean acceptRecommendation;
	String reviewComment;
	
	@PostConstruct
	public void init() { }
	
	public String addReview(){
		return null;
	}


}
