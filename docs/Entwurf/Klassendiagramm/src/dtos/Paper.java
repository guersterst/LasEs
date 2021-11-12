package dtos;

import java.util.List;

public class Paper {
	
	/*
	 * Important: There is no pointer to the pdf file here, it should be loaded seperatly from the database.
	 * 
	 * Important: Reviews soist soiba lod'n
	 */
	
	private List<Review> reviews;
	
	private Submission submission;
	
	private int id;
	
	private LocalDateTime uploadTime;
	
	private boolean visible;


}
