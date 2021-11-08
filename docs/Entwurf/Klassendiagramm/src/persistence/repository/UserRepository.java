package persistence.repository;

import java.util.List;

import dtos.User;
import global.util.ResultListParameters;
import persistence.exception.InvalidFieldsException;
import persistence.exception.InvalidQueryParamsException;

public class UserRepository {
	
	public static User getUser(String emailAddress) { return null; }
	
	public static boolean emailExists(String emailAddress) { return false; }
	
	public static List<User> getListWithSearchword(String searchWord, ResultListParameters resultParams) throws InvalidQueryParamsException { return null; }
	
	public static void editUser(String emailAddress, User newUser) throws InvalidFieldsException { }
	
	public static void addUser(User user) throws InvalidFieldsException { }

}
