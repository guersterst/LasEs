package persistence.repository;

import java.util.List;

import dtos.File;
import dtos.Paper;
import dtos.Review;
import global.util.ResultListParameters;
import persistence.exception.InvalidFieldsException;
import persistence.exception.InvalidQueryParamsException;

public class PaperRepository {
	
	// get, add, change, remove
	public static Paper get() { return null; }
	public static void add() {}
	public static void change() {}
	public static void remove() {}
	
	// get lists for
	public static List<Paper> getList() throws InvalidQueryParamsException { return null; }

	public static File getPDF() throws IllegalArgumentException { return null; }
	public static void setPDF() throws IllegalArgumentException {}
	
}
