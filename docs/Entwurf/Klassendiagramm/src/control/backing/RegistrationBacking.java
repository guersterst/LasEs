package control.backing;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;

import business.service.RegistrationService;
import control.internal.SessionInformation;

@RequestScoped
public class RegistrationBacking {
	
	private RegistrationService registrationService;
	
	private String passwordInput;
	
	private String titleInput;
	
	private String firstNameInput;
	
	private String lastNameInput;
	
	private String emailInput;

	private SessionInformation sessionInformation;
	
	@PostConstruct
	public void init() { }
	
	public String register() { return null; }

}
