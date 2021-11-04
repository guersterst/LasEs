package business.service;

import business.util.AvatarUtil;
import dtos.User;
import persistence.repository.UserRepository;

public class ProfileService {
	
	private AvatarUtil avatarUtil;
	
	private UserRepository userRepository;
	
	public void editUser(User newUser) { } 
	
	public void deleteUser(String email) { }
	
	public boolean emailExists(String email) { return false; }
	
	// byte[] gut?
	public void uploadAvatar(String email, byte[] avatar) { }
	
	public void deleteAvatar(String email) { }

}
