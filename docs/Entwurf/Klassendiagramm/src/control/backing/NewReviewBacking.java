package control.backing;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;

import business.service.ReviewService;
import control.internal.SessionInformation;

@RequestScoped
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
