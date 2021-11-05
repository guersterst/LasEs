package control.backing;

import business.internal.SessionInformation;

public class VerificationBacking {
	
	private LocalDateTime timerStart;
	
	private SessionInformation sessionInformation;
	
	public int secondsRemaining() { return 0; }

	public String redirect() { return null; }

}
