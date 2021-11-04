package business.service;

import java.util.List;

import dtos.Submission;
import persistence.repository.SubmissionRepository;
import persistence.repository.UserRepository;

public class SubmissionLieferando {
	
	private SubmissionRepository submissionRepository;
	
	private UserRepository userRepository;
	
	public List<Submission> getSubmissionsForForum(int forumId, String userEmail) { return null; }

}
