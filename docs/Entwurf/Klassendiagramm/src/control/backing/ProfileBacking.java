package control.backing;

import com.sun.xml.internal.ws.wsdl.writer.document.Part;

import business.service.ProfileService;
import dtos.User;
import idk.SessionInformation;

public class ProfileBacking {
	
	private SessionInformation sessionInformation;
	
	private ProfileService profileService;
	
	private User user;
	
	private User newUser;
	
	private Part uploadedAvatar;
	
	public void submitChanges() { }
	
	public String deleteProfile() { return null; }

}
