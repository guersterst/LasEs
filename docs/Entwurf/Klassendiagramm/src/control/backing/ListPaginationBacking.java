package control.backing;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;

@RequestScoped
public class ListPaginationBacking {
	
	private int pageSelectInput;
	
	@PostConstruct
	public void init() { }
	
	public void next() { }
	
	public void back() { }
	
	public void gotoPage() { }

}
