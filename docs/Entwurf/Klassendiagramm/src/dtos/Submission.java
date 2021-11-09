package dtos;
import java.util.List;

public class Submission {
	
	private List<Paper> papers;
	
	private ScientificForum scientificForum;
	
	private int id;
	
	private List<User> reviewers;
	
	private User author;
	
	private List<User> coAuthors;
	
	private User editor;
	
	
	private String title;
	
	private SubmissionState state;
	
	private LocalDateTime deadlineReviews;
	
	private LocalDateTime deadlineRevision;
	
	private LocalDateTime submissionTime;

}
