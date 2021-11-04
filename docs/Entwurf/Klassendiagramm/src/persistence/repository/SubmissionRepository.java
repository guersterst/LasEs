package persistence.repository;

import java.util.List;

import dtos.Submission;
import persistence.util.ConnectionPool;
import persistence.util.ResultListParameters;

public class SubmissionRepository {
	
	private ConnectionPool connectionPool;
	
	public List<Submission> getListWithSearchword(String searchword, ResultListParameters resultParams) throws InvalidQueryParamsException { return null; }

}
