package persistence.repository;

import java.util.List;

import dtos.Review;
import global.util.ResultListParameters;
import persistence.exception.InvalidFieldsException;
import persistence.exception.InvalidQueryParamsException;

public class ReviewRepository {
	
	public static List<Review> getReviewsForSubmission(int submissionId, ResultListParameters resultParams) throws InvalidQueryParamsException { return null; }
	
	public static void addReview(Review review) throws InvalidFieldsException { }

}
