package dtos;
import java.util.List;

public class Submission {
	
	private int id;
	
	private String title;
	
	private SubmissionState state;
	
	private LocalDateTime deadlineReviews;
	
	private LocalDateTime deadlineRevision;
	
	private LocalDateTime submissionTime;
	
	private List<Integer> papers;
	
	private int submitter;
	
	private int submittedIntoForum;
	
	private int editor;
	
	private List<Integer> coAuthors;
	
	private List<Integer> reviewers;

}
