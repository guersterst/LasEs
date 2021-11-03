package control.backing;

import com.sun.xml.internal.ws.wsdl.writer.document.Part;

import dtos.User;
import idk.SessionInformation;

public class ProfileBacking {
	
	private SessionInformation sessionInformation;
	
	private User user;
	
	private User newUser;
	
	private Part uploadedAvatar;
	
	public void submitChanges() { }
	
	public String deleteProfile() { return null; }

}
