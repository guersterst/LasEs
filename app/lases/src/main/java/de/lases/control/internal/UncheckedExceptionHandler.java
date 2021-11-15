package de.lases.control.internal;

import jakarta.el.ELException;
import jakarta.faces.FacesException;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.application.NavigationHandler;
import jakarta.faces.context.ExceptionHandler;
import jakarta.faces.context.ExceptionHandlerWrapper;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ExceptionQueuedEvent;
import jakarta.faces.event.ExceptionQueuedEventContext;

public class UncheckedExceptionHandler extends ExceptionHandlerWrapper {

    public UncheckedExceptionHandler(ExceptionHandler wrapped) {
        super(wrapped);
    }

    @Override
    public void handle() throws FacesException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        NavigationHandler navigationHandler
                = facesContext.getApplication().getNavigationHandler();

        for (ExceptionQueuedEvent event : getUnhandledExceptionQueuedEvents()) {
            ExceptionQueuedEventContext context =
                    (ExceptionQueuedEventContext) event.getSource();

            Throwable t = context.getException();

            while ((t instanceof FacesException || t instanceof ELException)
                    && t.getCause() != null) {
                t = t.getCause();
            }
            if (t instanceof ArithmeticException) {
                FacesMessage errorMessage = new FacesMessage("Arithmetic Exception");
                errorMessage.setSeverity(FacesMessage.SEVERITY_FATAL);
                FacesContext.getCurrentInstance().addMessage(null, errorMessage);
            } else if (t instanceof ArrayIndexOutOfBoundsException) {
                navigationHandler.handleNavigation(facesContext, null,
                        "/jsfconfig/caught-array-index-out-of-bound-exception" +
                                "?faces-redirect=true");
            }
        }
    }
}
