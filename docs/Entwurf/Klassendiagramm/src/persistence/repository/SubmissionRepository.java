package persistence.repository;

import java.util.List;

import dtos.Submission;
import global.util.ResultListParameters;
import persistence.exception.InvalidFieldsException;
import persistence.exception.InvalidQueryParamsException;

public class SubmissionRepository {
	
	// get, add, change, remove by id
	public static Submission get(Submission submission, Transaction transaction) throws InvalidFieldsException { return null; }
	public static void add(Submission submission, Transaction transaction) throws InvalidFieldsException { }
	public static void change(Submission old, Submission edited, Transaction transaction) throws InvalidFieldsException { }
	public static void remove(Submission submission, Transaction transaction) throws InvalidFieldsException { }
	
	
	// get lists
	public static List<Submission> getListWithSearchword(String searchword, ResultListParameters resultParams, Transaction transaction) throws InvalidQueryParamsException { return null; }
	public static List<Submission> getList(ResultListParameters resultParams, Transaction transaction) throws InvalidQueryParamsException { return null; }
	
	// get lists for
	public static List<Submission> getListForAuthor() { return null; }
	public static List<Submission> getListForCoAuthor() { return null; }
	public static List<Submission> getListForEditor() { return null; }
	public static List<Submission> getListForReviewer() { return null; }
	public static List<Submission> getListForScientificForum() { return null; }

}
