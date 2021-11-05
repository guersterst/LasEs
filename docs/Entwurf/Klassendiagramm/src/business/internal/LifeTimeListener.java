package business.internal;

import javax.servlet.annotation.WebListener;

@WebListener
public class LifeTimeListener {
	
	@Override
	public void contextInitialized() { }
	
	@Override
	public void contextDestroyed() { }
}
