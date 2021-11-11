package business.service;

import java.util.List;

import javax.faces.bean.ApplicationScoped;

import business.util.EmailUtil;
import dtos.ScientificForum;
import dtos.Submission;
import dtos.User;
import global.util.ResultListParameters;
import persistence.repository.ScientificForumRepository;

@ApplicationScoped
public class ScientificForumService {

	public ScientificForum getForum() { }

	public void updateForum(ScientificForum forum, ScientificForum newForum) { }

	public void addForum(ScientificForum forum) { }

	public void removeForum() { }

	public void addSubmission(Submission submission) { }
	
	public void removeSubmission(Submission submission) { }

	public void getSubmissions() { }
	
	public void addEditor(User user) { }

	public void removeEditor(User user) { }

	public void addScienceField() { }
	
	public List<User> getEditors() { return null; }
	
	public List<ScientificForum> getForums(ResultListParameters resultListParams) { return null; }
}
