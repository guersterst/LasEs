package persistence.repository;

import java.util.List;

import dtos.Submission;
import global.util.ResultListParameters;
import persistence.exception.InvalidFieldsException;
import persistence.exception.InvalidQueryParamsException;
import persistence.repository.connection_pool.ConnectionPool;

public class SubmissionRepository {
	
	public static List<Submission> getListWithSearchword(String searchword, ResultListParameters resultParams) throws InvalidQueryParamsException { return null; }
	
	public static List<Submission> getList(ResultListParameters resultParams) throws InvalidQueryParamsException { return null; }
	
	public static Submission getSubmission(int id) throws IllegalArgumentException { return null; }
	
	public static void addSubmission(Submission submission) throws InvalidFieldsException { }
	
	public static void changeSubmission(int submissionId, Submission edited) { }

}
