package persistence.repository;

import java.util.List;

import dtos.Review;
import global.util.ResultListParameters;
import persistence.exception.InvalidFieldsException;
import persistence.exception.InvalidQueryParamsException;
import persistence.util.ConnectionPool;

public class ReviewRepository {
	
	private ConnectionPool connectionPool;
	
	public List<Review> getReviewsForSubmission(int submissionId, ResultListParameters resultParams) throws InvalidQueryParamsException { return null; }
	
	public void addReview(Review review) throws InvalidFieldsException { }

}
