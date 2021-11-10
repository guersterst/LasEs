package control.backing;

import business.internal.SessionInformation;
import business.service.LoginService;

/**
 * This is the backing bean for welcome.xhtml. On that page, registered users can log in.
 */
public class WelcomeBacking {
	
	private LoginService loginService;
	
	private String emailInput;
	
	private String passwordInput;

	private SessionInformation sessionInformation;
	
	public String login() { }

}