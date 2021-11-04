package persistence.repository;

import java.util.List;

import dtos.Paper;
import global.util.ResultListParameters;
import persistence.exception.InvalidFieldsException;
import persistence.exception.InvalidQueryParamsException;
import persistence.exception.MissingFieldsException;
import persistence.util.ConnectionPool;

public class PaperRepository {
	
	private ConnectionPool connectionPool;
	
	public List<Paper> getList(ResultListParameters resultParams) throws InvalidQueryParamsException { return null; }

	public byte[] getPDFForPaper(int submissionId, int version) throws IllegalArgumentException { return null; }
	
	public void addPaper(Paper paper, byte[] pdf) throws InvalidFieldsException {
		
	}
	
}
