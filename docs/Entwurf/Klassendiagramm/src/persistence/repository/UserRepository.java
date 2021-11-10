package persistence.repository;

import java.util.List;

import dtos.Submission;
import dtos.User;
import global.util.ResultListParameters;
import persistence.exception.InvalidFieldsException;
import persistence.exception.InvalidQueryParamsException;

public class UserRepository {
	
	// get, add, change, remove by id
	public static User get(Submission submission, Transaction transaction) throws InvalidFieldsException { return null; }
	public static void add(Submission submission, Transaction transaction) throws InvalidFieldsException { }
	public static void change(Submission old, Submission edited, Transaction transaction) throws InvalidFieldsException { }
	public static void remove(Submission submission, Transaction transaction) throws InvalidFieldsException { }
	
	// get list for
	public static List<User> getList() { return null; }
	
	// specific adders for n to m
	public static void addScienceField() { }
	public static void removeScienceField() { }
	
	
	
	public static boolean emailExists(String emailAddress, Transaction transaction) { return false; }
	public static boolean isVerified() { return false; }

}
