package business.internal;

import java.util.List;
import java.util.Locale;

import javax.enterprise.context.SessionScoped;

import dtos.Privilege;

@SessionScoped
public class SessionInformation {
	private int userID;
	private List<Privilege> roles;
	private Locale locale;
	
	public void invalidateSession() { }
}
