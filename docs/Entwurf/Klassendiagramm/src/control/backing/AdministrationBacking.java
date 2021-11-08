package control.backing;

import javax.annotation.PostConstruct;

import business.service.CustomizationService;
import dtos.SystemSettings;

public class AdministrationBacking {

	private CustomizationService customizationService;

	private SystemSettings systemSettings;
	
	@PostConstruct
	public void init() { }

	public void update(){ }
}
