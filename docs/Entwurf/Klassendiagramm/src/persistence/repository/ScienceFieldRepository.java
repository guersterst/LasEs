package persistence.repository;

import java.util.List;

import dtos.ScienceField;
import dtos.ScientificForum;
import dtos.Submission;
import persistence.exception.InvalidFieldsException;

public class ScienceFieldRepository {
	
	// get, add, change, remove by id
	public static ScienceField get(Submission submission, Transaction transaction) throws InvalidFieldsException { return null; }
	public static void add(Submission submission, Transaction transaction) throws InvalidFieldsException { }
	public static void change(Submission old, Submission edited, Transaction transaction) throws InvalidFieldsException { }
	public static void remove(Submission submission, Transaction transaction) throws InvalidFieldsException { }
	
	
	public static List<ScienceField> getList(Transaction transaction) { return null; }
	public static boolean isScienceField(String name, Transaction transaction) { return false; }
	
	// get lists for
	public static List<ScienceField> getListForScientificForum() { return null; }
	public static List<ScienceField> getListForUser() { return null; }

}
