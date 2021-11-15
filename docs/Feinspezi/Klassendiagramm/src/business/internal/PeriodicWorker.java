package business.internal;

import javax.faces.bean.ApplicationScoped;

import persistence.util.DButils;

@ApplicationScoped
public class PeriodicWorker implements Runnable {

	@Override
	public void run() { }
	
	@Override 
	public void shutdown() { }

}
