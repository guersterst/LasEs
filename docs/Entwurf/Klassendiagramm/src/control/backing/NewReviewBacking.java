package control.backing;

import javax.annotation.PostConstruct;

import business.service.ReviewService;
import control.internal.SessionInformation;


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
