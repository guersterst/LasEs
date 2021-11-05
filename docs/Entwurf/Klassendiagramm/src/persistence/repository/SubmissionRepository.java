package persistence.repository;

import java.util.List;

import dtos.Submission;
import global.util.ResultListParameters;
import persistence.exception.InvalidFieldsException;
import persistence.exception.InvalidQueryParamsException;
import persistence.util.ConnectionPool;

public class SubmissionRepository {
	
	private ConnectionPool connectionPool;
	
	public List<Submission> getListWithSearchword(String searchword, ResultListParameters resultParams) throws InvalidQueryParamsException { return null; }
	
	public List<Submission> getList(ResultListParameters resultParams) throws InvalidQueryParamsException { return null; }
	
	public Submission getSubmission(int id) throws IllegalArgumentException { return null; }
	
	public void addSubmission(Submission submission) throws InvalidFieldsException { }
	
	public void changeSubmission(int submissionId, Submission edited) { }

}
