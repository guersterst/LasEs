package de.lases.control.backing;

import de.lases.business.internal.ConfigPropagator;
import de.lases.global.transport.ErrorMessage;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.PropertyResourceBundle;

/**
 * Backing bean for the error page.
 */
@RequestScoped
@Named
public class ErrorPageBacking {

    @Inject
    private FacesContext facesContext;

    @Inject
    private ConfigPropagator configPropagator;

    @Inject
    private PropertyResourceBundle bundle;

    private ErrorMessage errorMessage;

    /**
     * Check if the error message is initialized.
     */
    @PostConstruct
    public void init() {

        errorMessage = (ErrorMessage) facesContext.getExternalContext().getSessionMap().get("internal_error_message");
        facesContext.getExternalContext().getSessionMap().remove("internal_error_message");

        if (errorMessage == null) {

        int errorStatusCode = (Integer) facesContext.getExternalContext().getRequestMap()
                .get("jakarta.servlet.error.status_code");
        String requestErrorMessage = (String) facesContext.getExternalContext().getRequestMap()
                .get("jakarta.servlet.error.message");
        Exception requestException = (Exception) facesContext.getExternalContext().getRequestMap()
                .get("jakarta.servlet.error.exception");

            if (errorStatusCode == 404) {
                errorMessage = new ErrorMessage(bundle.getString("error.404"), requestErrorMessage);
            } else if (errorStatusCode == 500) {
                String developerMessage = requestErrorMessage;
                if (requestException != null) {
                    StringWriter sw = new StringWriter();
                    requestException.printStackTrace(new PrintWriter(sw, true));
                    developerMessage = sw.toString();
                }
                errorMessage = new ErrorMessage(bundle.getString("error.500"), developerMessage);
            }
        }
    }

    /**
     * Get the error message, including a stacktrace for the developer and an
     * error message for the user.
     *
     * @return Stacktrace and error message for the user encapsulated in one
     *         object.
     */
    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    /**
     * Set the full error message, including a stacktrace for the developer
     * and an error message for the user.
     *
     * @param errorMessage Stacktrace and error message for the user
     *                     encapsulated in one object.
     */
    public void setErrorMessage(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Returns if the file is in development mode, where stacktraces are shown.
     *
     * @return Is the app in development mode?
     */
    public boolean isDevelopmentMode() {
        return configPropagator.getProperty("DEBUG_AND_TEST_MODE").equals("true");
    }
}
