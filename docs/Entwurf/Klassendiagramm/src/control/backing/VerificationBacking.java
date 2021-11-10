package control.backing;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;

import control.internal.SessionInformation;

@RequestScoped
public class VerificationBacking {
	
	private LocalDateTime timerStart;
	
	private SessionInformation sessionInformation;
	
	@PostConstruct
	public void init() { }

	public String goToHome() { return null; }

}
