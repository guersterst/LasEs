package de.lases.control.internal;

import de.lases.control.exception.IllegalAccessException;
import de.lases.control.exception.IllegalUserFlowException;
import de.lases.global.transport.ErrorMessage;
import jakarta.el.ELException;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.faces.FacesException;
import jakarta.faces.application.NavigationHandler;
import jakarta.faces.context.ExceptionHandler;
import jakarta.faces.context.ExceptionHandlerWrapper;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ExceptionQueuedEvent;
import jakarta.faces.event.ExceptionQueuedEventContext;
import org.jboss.weld.exceptions.WeldException;

import java.util.Arrays;
import java.util.PropertyResourceBundle;
import java.util.logging.Logger;

/**
 * Handles all unchecked exceptions, which arrive in the {@code FacesServlet},
 * redirecting the user to an error-page.
 */
public class UncheckedExceptionHandler extends ExceptionHandlerWrapper {

    private final PropertyResourceBundle bundle;

    private final static Logger logger = Logger.getLogger(UncheckedExceptionHandler.class.getName());

    /**
     * Constructs an {@link UncheckedExceptionHandler}.
     * Should only be used by the according {@link UncheckedExceptionHandlerFactory}.
     *
     * @param wrapped The wrapped {@link ExceptionHandler}.
     */
    public UncheckedExceptionHandler(ExceptionHandler wrapped) {
        super(wrapped);
        bundle = CDI.current().select(PropertyResourceBundle.class).get();
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

        // Fetch context and navigation handler.
        FacesContext facesContext = FacesContext.getCurrentInstance();
        NavigationHandler navigationHandler
                = facesContext.getApplication().getNavigationHandler();

        for (ExceptionQueuedEvent event : getUnhandledExceptionQueuedEvents()) {
            ExceptionQueuedEventContext context =
                    (ExceptionQueuedEventContext) event.getSource();

            Throwable throwable = context.getException();

            // Unwrap JSF and dependency specific exceptions.
            while ((throwable instanceof FacesException
                    || throwable instanceof ELException
                    || throwable instanceof WeldException)
                    && throwable.getCause() != null) {
                throwable = throwable.getCause();
            }

            ErrorMessage errorMessage;
            if (throwable instanceof IllegalUserFlowException
            || throwable instanceof IllegalAccessException) {

                // Handle 404 exceptions.
                errorMessage = new ErrorMessage(bundle.getString("error.404"),
                        throwable.getMessage() + "\n" +
                                Arrays.toString(throwable.getStackTrace()));
            } else {

                // Handle 500 exceptions.
                errorMessage = new ErrorMessage(bundle.getString("error.500"),
                        throwable.getMessage() + "\n" +
                                Arrays.toString(throwable.getStackTrace()));
            }

            throwable.printStackTrace();
            facesContext.getExternalContext().getSessionMap().put("internal_error_message", errorMessage);
            navigationHandler.handleNavigation(facesContext, null,
                    "/views/anonymous/errorPage?faces-redirect=true");
            logger.severe("Handled an exception: \n" + throwable + "\n" + throwable.getMessage());
        }
    }
}
