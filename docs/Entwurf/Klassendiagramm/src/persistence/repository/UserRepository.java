package persistence.repository;

import dtos.User;
import persistence.exception.MissingFieldsException;
import persistence.util.ConnectionPool;

public class UserRepository {
	
	private ConnectionPool connectionPool;
	
	public User getUser(String emailAddress) { return null; }
	
	public void editUser(String emailAddress, User newUser) throws MissingFieldsException { }
	
	public void addUser(User user) throws MissingFieldsException { }

}
