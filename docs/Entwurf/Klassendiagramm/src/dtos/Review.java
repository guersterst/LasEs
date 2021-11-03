package dtos;

import java.time.LocalDateTime;

public class Review {
	
	/*
	 * PDF muast soiba lod'n.
	 */
	
	private int id;
	
	private LocalDateTime timestampUpdloaded;
	
	private boolean visible;
	
	private boolean acceptPaper;
	
	private String comment;
	
	private int paperId;
	
	private int submissionId;
	
	private int reviewerId;

}
