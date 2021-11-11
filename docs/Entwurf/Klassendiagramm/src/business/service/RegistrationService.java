package business.service;

import javax.faces.bean.ApplicationScoped;

import business.util.EmailUtil;
import business.util.Hashing;
import dtos.User;
import persistence.repository.UserRepository;

@ApplicationScoped
public class RegistrationService {
	
	public User selfRegister(User user) { return null; }
	
	public void registerByAdmin(User user) { }

}
