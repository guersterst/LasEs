package persistence.repository;

import java.util.List;

import dtos.ScientificForum;
import dtos.User;
import global.util.ResultListFilter;
import global.util.ResultListParameters;
import persistence.exception.InvalidFieldsException;
import persistence.exception.InvalidQueryParamsException;

public class ScientificForumRepository {
	
	public static List<ScientificForum> getList(ResultListParameters resultParams) throws InvalidQueryParamsException { return null; }
	
	public static List<ScientificForum> getListWithSearchword(String searchWord, ResultListParameters resultParams) throws InvalidQueryParamsException { return null; }
	
	public static void addScientificForum(ScientificForum newForum) throws InvalidFieldsException { }
	

}
