package control.backing;

import javax.annotation.PostConstruct;

public class ErrorPageBacking {
	
	@PostConstruct
	public void init() { }

    private String errorMessage() { return null; }

    public void setErrorMessage() { }

}
