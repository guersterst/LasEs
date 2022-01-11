package de.lases.control.backing;

import de.lases.global.transport.ErrorMessage;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.ProjectStage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

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
    private PropertyResourceBundle bundle;

    private ErrorMessage errorMessage;

    /**
     * Check if the error message is initialized.
     */
    @PostConstruct
    public void init() {
        Object sessionObject = facesContext.getExternalContext().getSessionMap().get("internal_error_message");
        if (sessionObject instanceof ErrorMessage) {
            errorMessage = (ErrorMessage) sessionObject;
            facesContext.getExternalContext().getSessionMap().remove("internal_error_message");
        } else {
            int errorStatusCode = -1;
            if (facesContext.getExternalContext().getRequestMap()
                    .get("jakarta.servlet.error.status_code") instanceof Integer) {
                errorStatusCode = (Integer) facesContext.getExternalContext().getRequestMap()
                        .get("jakarta.servlet.error.status_code");
            }
            String requestErrorMessage = "";
            if (facesContext.getExternalContext().getRequestMap()
                    .get("jakarta.servlet.error.message") instanceof String) {
                requestErrorMessage = (String) facesContext.getExternalContext().getRequestMap()
                        .get("jakarta.servlet.error.message");
            }

            if (errorStatusCode == 404) {
                errorMessage = new ErrorMessage(bundle.getString("error.404"), requestErrorMessage);
            } else {
                errorMessage = new ErrorMessage(bundle.getString("error.unknown"), requestErrorMessage);
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
        return !facesContext.isProjectStage(ProjectStage.Production);
    }
}
