package control.backing;

import com.sun.xml.internal.ws.wsdl.writer.document.Part;

import business.internal.SessionInformation;
import business.service.ProfileService;
import dtos.User;

public class ProfileBacking {
	
	private SessionInformation sessionInformation;
	
	private byte[] avatarUpload;
	
	private ProfileService profileService;
	
	private User user;
	
	private User newUser;
	
	private Part uploadedAvatar;
	
	public void submitChanges() { }
	
	public void uploadAvatar() { }
	
	public String deleteProfile() { return null; }
	
	public void deleteAvatar() { }

}
