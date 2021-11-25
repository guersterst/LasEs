package de.lases.control.backing;

import de.lases.business.internal.ConfigPropagator;
import de.lases.global.transport.ErrorMessage;
import de.lases.global.transport.UIMessage;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**
 * Backing bean for the error page.
 */
@RequestScoped
@Named
public class ErrorPageBacking {

    @Inject
    private ConfigPropagator configPropagator;

    private ErrorMessage errorMessage;

    /**
     * Initialize the error message dto.
     */
    @PostConstruct
    public void init() {
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
        return true;
    }
}
