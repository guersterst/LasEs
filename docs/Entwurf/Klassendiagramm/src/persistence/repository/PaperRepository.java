package persistence.repository;

import java.util.List;

import dtos.Paper;
import global.util.ResultListParameters;
import persistence.exception.InvalidFieldsException;
import persistence.exception.InvalidQueryParamsException;
import persistence.repository.connection_pool.ConnectionPool;

public class PaperRepository {
	
	public static List<Paper> getList(ResultListParameters resultParams) throws InvalidQueryParamsException { return null; }

	public static byte[] getPDFForPaper(int submissionId, int version) throws IllegalArgumentException { return null; }
	
	public static void addPaper(Paper paper, byte[] pdf) throws InvalidFieldsException {
		
	}
	
}
