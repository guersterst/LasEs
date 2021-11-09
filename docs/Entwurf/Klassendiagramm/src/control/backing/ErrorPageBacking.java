package control.backing;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;

@RequestScoped
public class ErrorPageBacking {
	
	@PostConstruct
	public void init() { }

    private String errorMessage() { return null; }

    public void setErrorMessage() { }

}
