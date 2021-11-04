package business.service;

import java.util.List;

import dtos.Submission;
import global.util.ResultListParameters;
import model.Privilege;
import persistence.repository.SubmissionRepository;
import persistence.repository.UserRepository;

public class SubmissionLieferando {
	
	private SubmissionRepository submissionRepository;
	
	private UserRepository userRepository;
	
	public List<Submission> getSubmissionsForForum(int forumId, String userEmail, ResultListParameters resultParams) { return null; }
	
	public List<Submission> getSubmissionsWithSearchword(String searchString, String userEmail, ResultListParameters resultParams) { return null; }
	
	public List<Submission> getSubmissionsForHomepage(String userEmail, Privilege role, ResultListParameters resultParams) { return null; }

}
