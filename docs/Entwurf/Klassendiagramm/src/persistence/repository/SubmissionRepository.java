package persistence.repository;

import java.util.List;

import dtos.Submission;
import global.util.ResultListParameters;
import persistence.exception.InvalidFieldsException;
import persistence.exception.InvalidQueryParamsException;

public class SubmissionRepository {
	
	public static List<Submission> getListWithSearchword(String searchword, ResultListParameters resultParams, Transaction transaction) throws InvalidQueryParamsException { return null; }
	
	public static List<Submission> getList(ResultListParameters resultParams, Transaction transaction) throws InvalidQueryParamsException { return null; }
	
	public static Submission getSubmission(int id, Transaction transaction) throws IllegalArgumentException { return null; }
	
	public static void addSubmission(Submission submission, Transaction transaction) throws InvalidFieldsException { }
	
	public static void changeSubmission(int submissionId, Submission edited, Transaction transaction) { }

}
