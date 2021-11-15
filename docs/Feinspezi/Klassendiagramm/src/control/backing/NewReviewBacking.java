package control.backing;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.servlet.http.Part;

import business.service.ReviewService;
import control.internal.SessionInformation;
import dtos.Review;

@RequestScoped
public class NewReviewBacking {
	
	private ReviewService reviewService;

	private SessionInformation sessionInformation;
	
	private Part uploadedPDF;
	
	private Review review;
	
	@PostConstruct
	public void init() { }
	
	public String addReview(){
		return null;
	}

}
