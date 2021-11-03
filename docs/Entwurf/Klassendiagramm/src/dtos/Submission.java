package dtos;

import java.time.LocalDateTime;
import java.util.List;

import model.SubmissionState;

public class Submission {
	
	private int id;
	
	private SubmissionState state;
	
	private LocalDateTime deadlineReviews;
	
	private LocalDateTime deadlineRevision;
	
	private LocalDateTime submissionTime;
	
	private List<Paper> papers;
	
	private int submitterId;
	
	private ScientificForum submittedIntoForum;
	
	private User editor;
	
	private User submitter;
	
	private List<User> coAuthors;
	
	private List<Integer> reviewerIds;

}
