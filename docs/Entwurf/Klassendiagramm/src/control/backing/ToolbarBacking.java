package control.backing;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;

import control.internal.SessionInformation;
import dtos.User;

/**
 * Backing bean for toolbar.xhtml. This view is the place where administrators and editors can administer a submission on the submission page.
 */
@RequestScoped
public class ToolbarBacking {
	
	private String reviewerEmailInput; // y
	
	private LocalDateTime reviewerDeadlineInput; // y
	
	private User editorInput; // y
	
	private User currentEditor; //y
	
	private LocalDateTime revisionDeadlineInput; //y
	

	private SessionInformation sessionInformation;
	
	private List<User> reviewer; // y
	
	private List<User> editors; // y
	
	
	
	@PostConstruct
	public void init() { }
	
	public void addReviewer() { } // y
	
	public String removeReviewer(int id) { return null; } // y
	
	public void chooseNewManagingEditor() { } // y
	
	public void requireRevision() { } // y
	
	public void acceptSubmission() { } // y
	
	public void rejectSubmission() { } // y
	
	public List<User> getReviewer() { return null; }
	
	public List<User> getEditors() { return null; }
	
	

}
