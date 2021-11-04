package control.backing;

import java.util.List;

import business.service.LoginService;
import dtos.ScientificForum;
import dtos.Submission;
import dtos.User;
import idk.SessionInformation;

public class NavigationBacking {
	
	private SessionInformation sessionInformation;
	
	private String searchString;
	
	private LoginService loginService;
	
	private List<User> userSearchResults;
	
	private List<ScientificForum> forumSearchResults;
	
	private List<Submission> submissionSearchResults;
	
	public List<User> getUserSearchResults() { return null; }
	
	public List<ScientificForum> getScientificForums() { return null; }
	
	public List<Submission> getSubmissionSearchResults() { return null; }

	
	public String logout() { return null; }
	
	public String search() { return null; }

}
