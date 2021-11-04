package persistence.util;

/**
 * Encapsulates all values a repository needs to configure the result lists on queries with a list of results. These parameters include the column to sort by and the offset and length of the desired part of the result.
 */
public class ResultListParameters {
	
	private int resultOffset;
	
	private int resultLength;
	
	private String sortColumn;

}
