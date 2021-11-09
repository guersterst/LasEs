package control.backing;

import javax.annotation.PostConstruct;

import com.sun.xml.internal.ws.wsdl.writer.document.Part;

import business.service.ProfileService;
import control.internal.SessionInformation;
import dtos.User;

public class ProfileBacking {
	
	private SessionInformation sessionInformation;
	
	private byte[] avatarUpload;
	
	private ProfileService profileService;
	
	private User user;
	
	private User newUser;
	
	private Part uploadedAvatar;
	
	@PostConstruct
	public void init() { }
	
	public void submitChanges() { }
	
	public void uploadAvatar() { }
	
	public String deleteProfile() { return null; }
	
	public void deleteAvatar() { }

}
