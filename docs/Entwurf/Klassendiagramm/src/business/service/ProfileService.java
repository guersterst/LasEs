package business.service;

import business.util.AvatarUtil;
import dtos.User;
import persistence.repository.UserRepository;

public class ProfileService {
	
	private AvatarUtil avatar;
	
	private UserRepository userRepository;
	
	public void editUser(User newUser) { } 
	
	public boolean emailExists(String email) { return false; }

}
