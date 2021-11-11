package control.backing;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;

import business.service.LoginService;
import control.internal.SessionInformation;

/**
 * This is the backing bean for welcome.xhtml. On that page, registered users can log in.
 */
@RequestScoped
public class WelcomeBacking {
	
	private LoginService loginService;
	
	private String emailInput; // y
	
	private String passwordInput; // y

	private SessionInformation sessionInformation;
	
	@PostConstruct
	public void init() { }
	
	public String login() { return null; } // y

}
