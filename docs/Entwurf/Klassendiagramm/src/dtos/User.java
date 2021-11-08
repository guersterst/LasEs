package dtos;

import java.util.List;

public class User {
	
	private int id;
	
	private List<Privilege> priveleges;
	
	private String title;
	
	private String firstName;
	
	private String lastName;
	
	private String emailAddress;
	
	private String unvalidatedEmailAddress;
	
	private LocalDateTime emailValidationStarted;
	
	private LocalDate dateOfBirth;
	
	private List<Integer> interests;
	
	private String employer;

	private byte[] avatar;
	
	private byte[] avatarThumbnail;
	
	private String passwordHashed;
	
	private String passwordSalt;
	

}
