package business.internal;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SystemSettings {

	private ExceptionQueue exceptionQueue;

	private SystemConfiguration config;
	
	public void load() { }
	
	public void write() { }
}
