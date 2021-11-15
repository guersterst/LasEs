package control.backing;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;

@RequestScoped
public class ErrorPageBacking {
	
	private String errorMessage;
	
	private String stackTrace;
	
	@PostConstruct
	public void init() { }
	

}
