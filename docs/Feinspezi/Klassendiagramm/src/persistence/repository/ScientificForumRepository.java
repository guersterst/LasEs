package persistence.repository;

import java.util.List;

import dtos.ScientificForum;
import dtos.Submission;
import dtos.User;
import global.util.ResultListFilter;
import global.util.ResultListParameters;
import persistence.exception.InvalidFieldsException;
import persistence.exception.InvalidQueryParamsException;

public class ScientificForumRepository {
	
	// get, add, change, remove by id
	public static ScientificForum get(Submission submission, Transaction transaction) throws InvalidFieldsException { return null; }
	public static void add(Submission submission, Transaction transaction) throws InvalidFieldsException { }
	public static void change(Submission old, Submission edited, Transaction transaction) throws InvalidFieldsException { }
	public static void remove(Submission submission, Transaction transaction) throws InvalidFieldsException { }
	
	// get lists
	public static List<ScientificForum> getList(ResultListParameters resultParams, Transaction transaction) throws InvalidQueryParamsException { return null; }
	
	// specific adders for n to m
	public static void addEditor() { }
	public static void addScienceField() { }
	public static void removeEditor() { }
	public static void removeScienceField() { }
	

}
