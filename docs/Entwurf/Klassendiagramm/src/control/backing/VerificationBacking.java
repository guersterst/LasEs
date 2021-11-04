package control.backing;

import java.time.LocalDateTime;

import idk.SessionInformation;

public class VerificationBacking {
	
	private LocalDateTime timerStart;
	
	private SessionInformation sessionInformation;
	
	public int secondsRemaining() { return 0; }

	public String redirect() { return null; }

}
