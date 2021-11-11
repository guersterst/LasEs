package business.service;

import business.util.Hashing;
import dtos.User;
import persistence.repository.UserRepository;

@ApplicationScoped
public class LoginService {
	
	private Hashing hashing;

	public User login(String email, String password) { return null; }
}
