package business.service;

import java.util.List;

import business.util.AvatarUtil;
import dtos.User;
import global.util.ResultListParameters;
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
	
	public List<User> getUsersSearch(String searchString) { return null; }
	
	public List<User> getUsers(String searchString, ResultListParameters resultListParams) { return null; }

}
