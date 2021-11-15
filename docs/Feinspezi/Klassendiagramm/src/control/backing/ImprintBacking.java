package control.backing;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;

@RequestScoped
public class ImprintBacking {
	
	private String imprint;
	
	@PostConstruct
	public void init() { }

}
