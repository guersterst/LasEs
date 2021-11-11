package control.backing;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.servlet.http.Part;

import business.service.ScientificForumService;
import business.service.SubmissionService;
import business.service.UserService;
import control.internal.SessionInformation;
import dtos.User;

@RequestScoped
public class NewSubmissionBacking {

	
	// Services and Session.
	
	private SessionInformation sessionInformation;
	
	private SubmissionService submissionService;
	
	private ScientificForumService scientificForumService;
	
	private UserService userService;
	
	// Inputs from view.
	
	private List<User> coAuthors;
	
	private List<User> editors;
	
	private String nameOfPaperInput;
	
	private String forumNameInput;
	
	private User editorSelectionInput;
	
	private Part uploadedPDF;
	
	private String titleInput;
	
	private String coAuthorTitleInput;
	
	private String coAuthorFirstNameInput;
	
	private String coAuthorLastNameInput;
	
	private String coAuthorEmailInput;
	
	@PostConstruct
	public void init() { }
	
	// Action methods.
	
	public void submitCoAuthor() { }
	
	public void deleteCoAuthor(int id) { }
	
	public void submit() { }

}
