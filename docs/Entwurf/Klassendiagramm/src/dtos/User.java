package dtos;

import java.util.List;

public class User {
	
	private List<Privilege> priveleges;
	
	private String title;
	
	private String firstName;
	
	private String lastName;
	
	private String emailAddress;
	
	private String unvalidatedEmailAddress;
	
	private LocalDateTime emailValidationStarted;
	
	private int id;
	
	private LocalDate dateOfBirth;
	
	private List<ScienceField> interests;
	
	private String employer;

	private byte[] avatar;
	
	private byte[] avatarThumbnail;
	
	private String passwordHashed;
	
	private String passwordSalt;
	

}