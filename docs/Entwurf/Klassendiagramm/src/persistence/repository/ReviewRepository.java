package persistence.repository;

import java.util.List;

import dtos.Review;
import global.util.ResultListParameters;
import persistence.exception.InvalidFieldsException;
import persistence.exception.InvalidQueryParamsException;

public class ReviewRepository {
	
	// get, add, change, remove
	public static Review get(Review review, Transaction transaction) throws InvalidFieldsException { return null; }
	public static void add() {}
	public static void change() {}
	public static void remove() {}
	
	// ueberladen fuer user und paper
	public static List<Review> getList() { return null; }

}
