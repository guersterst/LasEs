package persistence.repository;

import java.util.List;

import dtos.User;
import global.util.ResultListParameters;
import persistence.exception.InvalidFieldsException;
import persistence.exception.InvalidQueryParamsException;
import persistence.exception.MissingFieldsException;
import persistence.util.ConnectionPool;

public class UserRepository {
	
	private ConnectionPool connectionPool;
	
	public User getUser(String emailAddress) { return null; }
	
	public boolean emailExists(String emailAddress) { return false; }
	
	public List<User> getListWithSearchword(String searchWord, ResultListParameters resultParams) throws InvalidQueryParamsException { return null; }
	
	public void editUser(String emailAddress, User newUser) throws InvalidFieldsException { }
	
	public void addUser(User user) throws InvalidFieldsException { }

}
