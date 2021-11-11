package control.backing;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.servlet.http.Part;

import business.service.ReviewService;
import control.internal.SessionInformation;

@RequestScoped
public class NewReviewBacking {
	
	private ReviewService reviewService;

	private SessionInformation sessionInformation;
	
	private Part uploadedPDF;

	private boolean acceptRecommendation;
	String reviewComment;
	
	@PostConstruct
	public void init() { }
	
	public String addReview(){
		return null;
	}
	
	public void uploadPDF() { }


}
