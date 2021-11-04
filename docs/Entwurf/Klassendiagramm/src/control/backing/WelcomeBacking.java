package control.backing;

import business.service.LoginService;
import idk.SessionInformation;

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
