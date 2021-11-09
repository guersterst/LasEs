package business.service;

import business.util.Hashing;
import dtos.User;
import persistence.repository.UserRepository;

public class LoginService {
	
	private Hashing hashing;

	private ExceptionQueue exceptionQueue;
	
	public User login(String email, String password) { return null; }
}
