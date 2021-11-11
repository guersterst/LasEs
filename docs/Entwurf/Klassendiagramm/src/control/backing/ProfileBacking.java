package control.backing;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;

import com.sun.xml.internal.ws.wsdl.writer.document.Part;

import business.service.ProfileService;
import business.service.UserService;
import control.internal.SessionInformation;
import dtos.User;

@RequestScoped
public class ProfileBacking {
	
	private SessionInformation sessionInformation;
	
	private Part uploadedAvatar;
	
	private UserService userService;
	
	private User user;
	
	private User newUser;
	
	@PostConstruct
	public void init() { }
	
	public void submitChanges() { }
	
	public void uploadAvatar() { }
	public void deleteAvatar() { }
	
	public String deleteProfile() { return null; }

}
