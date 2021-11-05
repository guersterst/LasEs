package control.backing;

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


	public String abort(){ }

	public String saveUser(){ }
}
