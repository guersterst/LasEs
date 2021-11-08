package control.backing;

import javax.annotation.PostConstruct;

import business.service.CustomizationService;

public class InitialConfigBacking {
	
	private CustomizationService customizationService;
	
	@PostConstruct
	public void init() { }
	
	public String getConnectionState() { return null; }
	
	public void createDb() { }

}
