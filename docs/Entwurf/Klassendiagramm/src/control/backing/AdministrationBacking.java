package control.backing;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.servlet.http.Part;

import business.service.CustomizationService;
import dtos.SystemSettings;

@RequestScoped
public class AdministrationBacking {

	private CustomizationService customizationService;

	private SystemSettings systemSettings;
	
	private Part uploadedLogo;
	
	@PostConstruct
	public void init() { }

	public void save(){ }
	
	public void abort() { }

}
