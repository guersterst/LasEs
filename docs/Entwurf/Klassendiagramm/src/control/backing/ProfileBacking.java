package control.backing;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;

import com.sun.xml.internal.ws.wsdl.writer.document.Part;

import business.service.ProfileService;
import business.service.ScienceFieldService;
import business.service.SubmissionService;
import business.service.UserService;
import control.internal.SessionInformation;
import dtos.ScienceField;
import dtos.User;

@RequestScoped
public class ProfileBacking {
	
	private SessionInformation sessionInformation;
	
	private Part uploadedAvatar;
	
	private UserService userService;
	
	private SubmissionService submissionService;
	
	private ScienceFieldService scienceFieldService;
	
	private User user;
	
	private User newUser;
	
	private List<ScienceField> selectedScienceFields;
	
	private List<ScienceField> scienceFields;
	
	private ScienceField selectedScienceField;
	
	private int numberOfSubmissions;
	
	private String passwordInput;
	
	private boolean popupShown;
	
	@PostConstruct
	public void init() { }
	
	public void submitChanges() { }
	
	public void uploadAvatar() { }
	public void deleteAvatar() { }
	public void addScienceField() { }
	public void deleteScienceField(int id) { }
	public void abortInPopup() { }
	public void saveInPopup() { }
	
	public String deleteProfile() { return null; }

}
