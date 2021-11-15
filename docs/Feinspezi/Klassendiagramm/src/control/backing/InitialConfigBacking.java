package control.backing;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;

import business.service.CustomizationService;

@RequestScoped
public class InitialConfigBacking {
	
	private CustomizationService customizationService;
	
	private String dbConnectionState;
	
	@PostConstruct
	public void init() { }
	
	public void createDb() { }

}
