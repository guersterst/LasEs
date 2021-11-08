package control.backing;

import java.util.List;

import javax.annotation.PostConstruct;

import business.internal.SessionInformation;
import dtos.User;

/**
 * Backing bean for toolbar.xhtml. This view is the place where administrators and editors can administer a submission on the submission page.
 */
public class ToolbarBacking {
	
	private List<User> reviewer;
	
	private List<User> editors;
	
	private String reviewerEmailInput;
	
	private String managingEditorSelection;

	private SessionInformation sessionInformation;
	
	@PostConstruct
	public void init() { }
	
	public void addReviewer() { }
	
	public void removeReviewer(int id) { }
	
	public void chooseNewManagingEditor() { }
	
	public void requireRevision() { }
	
	public void acceptSubmission() { }
	
	public void rejectSubmission() { }
	
	public List<User> getReviewer() { return null; }
	
	public List<User> getEditors() { return null; }
	
	

}
