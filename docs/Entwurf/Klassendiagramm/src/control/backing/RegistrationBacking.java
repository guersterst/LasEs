package control.backing;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;

import business.service.RegistrationService;
import control.internal.SessionInformation;

@RequestScoped
public class RegistrationBacking {
	
	private RegistrationService registrationService;
	
	private User newUser;

	private SessionInformation sessionInformation;
	
	@PostConstruct
	public void init() { }
	
	public String register() { return null; } // y

}
