package business.service;

import business.util.Hashing;
import dtos.User;
import persistence.repository.UserRepository;

public class LoginService {
	
	private UserRepository userRepository;
	
	private Hashing hashing;
	
	public User login(String email, String password) { return null; }
	
	public void logout() { }

}
