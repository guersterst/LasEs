package control.exception;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeListenerProxy;

import javax.faces.bean.SessionScoped;

@SessionScoped
public class ExceptionHandler extends PropertyChangeListenerProxy implements PropertyChangeListener {

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		// TODO Automatisch erstellter Methoden-Stub
		
	}

}
