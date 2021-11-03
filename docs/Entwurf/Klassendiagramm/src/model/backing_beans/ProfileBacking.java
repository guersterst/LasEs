package model.backing_beans;

import com.sun.xml.internal.ws.wsdl.writer.document.Part;

import dtos.User;
import idk.SessionInformation;

public class ProfileBacking {
	
	private User user;
	
	private User newUser;
	
	private Part uploadedAvatar;

	public Part getUploadedAvatar() {
		return uploadedAvatar;
	}

	public void setUploadedAvatar(Part uploadedAvatar) {
		this.uploadedAvatar = uploadedAvatar;
	}

	public User getNewUser() {
		return newUser;
	}

	public void setNewUser(User newUser) {
		this.newUser = newUser;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public void submitChanges() { }
	
	public String deleteProfile() { return null; }

	private SessionInformation sessionInformation;

}
