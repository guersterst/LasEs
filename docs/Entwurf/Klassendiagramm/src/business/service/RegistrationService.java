package business.service;

import business.util.EmailUtil;
import business.util.Hashing;
import dtos.User;
import persistence.repository.UserRepository;

@ApplicationScoped
public class RegistrationService {
	
	private Hashing hashing;
	
	private EmailUtil emailUtil;
	
	public User selfRegister(User user) { return null; }
	
	public void registerByAdmin(User user) { }

}
