package control.backing;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;

import business.service.RegistrationService;
import control.internal.SessionInformation;

@RequestScoped
public class RegistrationBacking {
	
	private RegistrationService registrationService;
	
	private String passwordInput; // y
	
	private String titleInput; // y
	
	private String firstNameInput; // 7
	
	private String lastNameInput; // 7
	
	private String emailInput; // y

	private SessionInformation sessionInformation;
	
	@PostConstruct
	public void init() { }
	
	public String register() { return null; } // y

}
