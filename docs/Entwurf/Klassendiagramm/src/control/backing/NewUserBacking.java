package control.backing;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;

import business.service.RegistrationService;

@RequestScoped
public class NewUserBacking {
	
	private RegistrationService registrationService;

	private String password;

	private String firstName;

	private String lastName;

	private String title;

	private String email;

	private boolean isAdmin;

	
	@PostConstruct
	public void init() { }

	public String abort(){ return null; }

	public String saveUser(){ return null; }
}
