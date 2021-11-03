package dtos;

import java.time.LocalDate;
import java.util.List;

import model.Privileges;

public class User {
	
	private Privileges priveleges;
	
	private String title;
	
	private String firstName;
	
	private String lastName;
	
	private String emailAddress;
	
	private int id;
	
	private LocalDate dateOfBirth;
	
	private List<ScienceField> scienceFields;
	
	// Arbeitgeber

	
	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private byte[] avatar;
	
	private byte[] avatarThumbnail;

	public byte[] getAvatarThumbnail() {
		return avatarThumbnail;
	}

	public void setAvatarThumbnail(byte[] avatarThumbnail) {
		this.avatarThumbnail = avatarThumbnail;
	}

	public byte[] getAvatar() {
		return avatar;
	}

	public void setAvatar(byte[] avatar) {
		this.avatar = avatar;
	}

	public Privileges getPriveleges() {
		return priveleges;
	}

	public void setPriveleges(Privileges priveleges) {
		this.priveleges = priveleges;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	

}
