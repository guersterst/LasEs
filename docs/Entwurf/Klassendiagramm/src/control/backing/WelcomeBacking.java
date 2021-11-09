package control.backing;

import javax.annotation.PostConstruct;

import business.service.LoginService;
import control.internal.SessionInformation;

/**
 * This is the backing bean for welcome.xhtml. On that page, registered users can log in.
 */
public class WelcomeBacking {
	
	private LoginService loginService;
	
	private String emailInput;
	
	private String passwordInput;

	private SessionInformation sessionInformation;
	
	@PostConstruct
	public void init() { }
	
	public String login() { }

}
