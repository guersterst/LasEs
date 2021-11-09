package control.backing;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;

import business.service.CustomizationService;
import dtos.SystemSettings;

@RequestScoped
public class AdministrationBacking {

	private CustomizationService customizationService;

	private SystemSettings systemSettings;
	
	@PostConstruct
	public void init() { }

	public void update(){ }
}
