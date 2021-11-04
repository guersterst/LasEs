package persistence.repository;

import java.time.LocalDateTime;
import java.util.List;

import dtos.ScientificForum;
import dtos.User;
import global.util.ResultListFilter;
import global.util.ResultListParameters;
import persistence.exception.InvalidFieldsException;
import persistence.exception.InvalidQueryParamsException;
import persistence.exception.MissingFieldsException;
import persistence.util.ConnectionPool;

public class ScientificForumRepository {
	
	private ConnectionPool connectionPool;
	
	public List<ScientificForum> getList(ResultListParameters resultParams) throws InvalidQueryParamsException { return null; }
	
	public List<ScientificForum> getListWithSearchword(String searchWord, ResultListParameters resultParams) throws InvalidQueryParamsException { return null; }
	
	public void addScientificForum(ScientificForum newForum) throws InvalidFieldsException { }
	

}
