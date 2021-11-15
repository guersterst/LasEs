package control.internal;

import java.util.List;
import java.util.Locale;

import javax.enterprise.context.SessionScoped;

import dtos.Privilege;

@SessionScoped
public class SessionInformation extends Serializable {
	private int userID;
	private List<Privilege> roles;
	private Locale locale;
	
	public void invalidateSession() { }
}
