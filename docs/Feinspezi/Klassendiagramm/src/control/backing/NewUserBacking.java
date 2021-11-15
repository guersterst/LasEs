package control.backing;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;

import business.service.RegistrationService;
import dtos.User;

@RequestScoped
public class NewUserBacking {
	
	private RegistrationService registrationService;

	private User newUser;

	
	@PostConstruct
	public void init() { }

	public String abort(){ return null; }

	public String saveUser(){ return null; }
}
