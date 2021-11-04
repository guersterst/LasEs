package business.service;

import business.util.EmailService;
import dtos.User;
import persistence.repository.UserRepository;

public class RegistrationService {
	
	private UserRepository userRepository;
	
	private EmailService emailService;
	
	public User selfRegister(User user) { return null; }
	
	public void registerByAdmin(User user) { }

}
