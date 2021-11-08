package persistence.repository;

import java.util.List;

import dtos.User;
import global.util.ResultListParameters;
import persistence.exception.InvalidFieldsException;
import persistence.exception.InvalidQueryParamsException;

public class UserRepository {
	
	public static User getUser(String emailAddress, Transaction transaction) { return null; }
	
	public static boolean emailExists(String emailAddress, Transaction transaction) { return false; }
	
	public static List<User> getListWithSearchword(String searchWord, ResultListParameters resultParams, Transaction transaction) throws InvalidQueryParamsException { return null; }
	
	public static void editUser(String emailAddress, User newUser, Transaction transaction) throws InvalidFieldsException { }
	
	public static void addUser(User user, Transaction transaction) throws InvalidFieldsException { }

}
