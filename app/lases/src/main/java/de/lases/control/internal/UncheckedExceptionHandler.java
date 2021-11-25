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

/**
 * Handles all unchecked exceptions, which arrive in the {@code FacesServlet},
 * redirecting the user to an error-page.
 */
public class UncheckedExceptionHandler extends ExceptionHandlerWrapper {

    /**
     * Constructs an {@link UncheckedExceptionHandler}.
     * Should only be used by the according {@link UncheckedExceptionHandlerFactory}.
     *
     * @param wrapped The wrapped {@link ExceptionHandler}.
     */
    public UncheckedExceptionHandler(ExceptionHandler wrapped) {
        super(wrapped);
    }

    /**
     * Takes appropriate action for each unchecked exception.
     *
     * <ul>
     *     <li> Redirect to the error-page.</li>
     *     <li> Display of an appropriate error-message.
     *     This may contain the stacktrace if the configuration demands it.</li>
     * </ul>
     * This method is a listener and does not need to be called.
     */
    @Override
    public void handle() {
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
