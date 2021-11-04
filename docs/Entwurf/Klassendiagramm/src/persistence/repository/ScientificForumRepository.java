package persistence.repository;

import java.time.LocalDateTime;
import java.util.List;

import dtos.ScientificForum;
import dtos.User;
import persistence.exception.InvalidFieldsException;
import persistence.exception.InvalidQueryParamsException;
import persistence.exception.MissingFieldsException;
import persistence.util.ConnectionPool;
import persistence.util.ResultListParameters;

public class ScientificForumRepository {
	
	private ConnectionPool connectionPool;
	
	public List<ScientificForum> getList(ResultListParameters resultParams) throws InvalidQueryParamsException { return null; }
	
	public List<ScientificForum> getListWithSearchword(String searchWord, ResultListParameters resultParams) throws InvalidQueryParamsException { return null; }
	
	public List<ScientificForum> getListDeadlineGTE(LocalDateTime deadline, ResultListParameters resultParams) throws InvalidQueryParamsException { return null; }
	
	public List<ScientificForum> getListDeadlineLTE(LocalDateTime deadline, ResultListParameters resultParams) throws InvalidQueryParamsException { return null; }
	
	public List<ScientificForum> getListDeadlineBetween(LocalDateTime deadlineLowerBound, LocalDateTime deadlineHigherBound, ResultListParameters resultParams) throws InvalidQueryParamsException { return null; }
	
	public List<ScientificForum> getListWithEditors(List<String> editors, ResultListParameters resultParams) throws InvalidQueryParamsException { return null; }
	
	public List<ScientificForum> getListWithTopics(List<String> topics, ResultListParameters resultParams) throws InvalidQueryParamsException { return null; }
	
	public void addScientificForum(ScientificForum newForum) throws InvalidFieldsException { }
	

}
