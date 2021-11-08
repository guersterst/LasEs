package business.service;

import java.util.List;

import business.util.EmailUtil;
import dtos.ScientificForum;
import dtos.Submission;
import dtos.User;
import global.util.ResultListParameters;
import persistence.repository.ScientificForumRepository;

public class ScientificForumService {
	
	private EmailUtil emailUtil;
	
	public void addSubmission(Submission submission) { }
	
	public void removeSubmission(Submission submission) { }
	
	public void updateForum(ScientificForum forum, ScientificForum newForum) { }
	
	public void addEditor(User user) { }
	
	public List<User> getEditors() { return null; }
	
	public void addScientificForum(ScientificForum forum) { }
	
	public List<ScientificForum> getAll(ResultListParameters resultListParams) { return null; }
	
	public List<ScientificForum> getAllSearch(String searchString, ResultListParameters resultListParams) { return null; }


}
