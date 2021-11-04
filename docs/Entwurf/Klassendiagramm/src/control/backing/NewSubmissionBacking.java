package control.backing;

import java.util.List;

import business.service.SubmissionService;
import dtos.User;
import idk.SessionInformation;

public class NewSubmissionBacking {

	
	// Services and Session.
	
	private SessionInformation sessionInformation;
	
	private SubmissionService submissionService;
	
	// Inputs from view.
	
	private List<User> coAuthors;
	
	private String nameOfPaperInput;
	
	private String forumNameInput;
	
	private int editorSelectionInput;
	
	private byte[] pdfUpload;
	
	private String titleInput;
	
	private String coAuthorFirstNameInput;
	
	private String coAuthorLastNameInput;
	
	private String coAuthorEmailInput;
	
	// Action methods.
	
	public void uploadPDF() { }
	
	public void submitCoAuthor() { }
	
	public void submit() { }

}
