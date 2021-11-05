package dtos;

public class Paper {
	
	/*
	 * Important: There is no pointer to the pdf file here, it should be loaded seperatly from the database.
	 * 
	 * Important: Reviews soist soiba lod'n
	 */
	
	private int version;
	
	private LocalDateTime uploadTime;
	
	private boolean visible;
	
	private int submissionId;


}
