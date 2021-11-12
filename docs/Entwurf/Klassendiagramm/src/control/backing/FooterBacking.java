package control.backing;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;

import control.internal.SessionInformation;

@SessionScoped
public class FooterBacking implements Serializable {
	
	private SessionInformation sessionInformation;
	
	public String getLanguageString() { return null; }
	
}
