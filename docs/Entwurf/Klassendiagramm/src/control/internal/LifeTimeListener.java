package control.internal;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class LifeTimeListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Automatisch erstellter Methoden-Stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Automatisch erstellter Methoden-Stub
		
	}
}
