package control.backing;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;

import business.service.CustomizationService;
import business.service.LoginService;
import control.internal.SessionInformation;
import dtos.SystemSettings;

/**
 * This is the backing bean for welcome.xhtml. On that page, registered users can log in.
 */
@RequestScoped
public class WelcomeBacking {
	
	private LoginService loginService;
	
	private String emailInput; // y
	
	private String passwordInput; // y
	
	private SystemSettings systemSettings;
	
	private CustomizationService customizationService;

	private SessionInformation sessionInformation;
	
	@PostConstruct
	public void init() { }
	
	public String login() { return null; } // y

}
