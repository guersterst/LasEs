package business.service;

import java.util.List;

import business.util.EmailUtil;
import dtos.ScientificForum;
import dtos.Submission;
import dtos.User;
import global.util.ResultListParameters;
import persistence.repository.ScientificForumRepository;

public class ScientificForumService {

	private ExceptionQueue exceptionQueue;

	private EmailUtil emailUtil;

	private SubmissionRepository submissionRepository;

	private ScientificForumRepository scientificForumRepository;
	
	public void addSubmission(Submission submission) { }
	
	public void removeSubmission(Submission submission) { }
	
	public void addEditor(User user) { }

	public void removeEditor(User user) { }

	public int numberOfEditors() { }
	
	public List<User> getEditors() { return null; }

	public void updateForum(ScientificForum forum, ScientificForum newForum) { }

	public void addForum(ScientificForum forum) { }
	
	public List<ScientificForum> getAll(ResultListParameters resultListParams) { return null; }
	
	public List<ScientificForum> getAllSearch(String searchString, ResultListParameters resultListParams) { return null; }
}
