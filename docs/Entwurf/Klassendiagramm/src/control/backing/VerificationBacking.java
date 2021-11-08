package control.backing;

import javax.annotation.PostConstruct;

import business.internal.SessionInformation;

public class VerificationBacking {
	
	private LocalDateTime timerStart;
	
	private SessionInformation sessionInformation;
	
	@PostConstruct
	public void init() { }
	
	public int secondsRemaining() { return 0; }

	public String redirect() { return null; }

}
