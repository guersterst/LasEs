package control.backing;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;

import business.service.LoginService;
import control.internal.SessionInformation;
import dtos.ScientificForum;
import dtos.Submission;
import dtos.User;


@RequestScoped
public class NavigationBacking {
	
	private String searchString; // y
	
	private LoginService loginService;
	
	private SessionInformation sessionInformation;
	
	private List<User> userSearchResults;
	
	private List<ScientificForum> forumSearchResults;
	
	private List<Submission> submissionSearchResults;
	
	public List<User> getUserSearchResults() { return null; }
	
	public List<ScientificForum> getScientificForums() { return null; }
	
	public List<Submission> getSubmissionSearchResults() { return null; }

	
	@PostConstruct
	public void init() { }
	
	public String logout() { return null; } // y
	
	public String search() { return null; } // y

}
