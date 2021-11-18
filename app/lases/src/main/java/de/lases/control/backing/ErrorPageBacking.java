package de.lases.control.backing;

import de.lases.global.transport.ErrorMessage;
import de.lases.global.transport.UIMessage;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

/**
 * Backing bean for the error page.
 */
@RequestScoped
@Named
public class ErrorPageBacking {

    private ErrorMessage errorMessage;

    @PostConstruct
    public void init() {
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    /**
     * Set the full error message, including a stacktrace for the developer
     * and an error message for the use.
     *
     * @param errorMessage Stacktrace and error message for the user
     *                     encapsulated in one object.
     */
    public void setErrorMessage(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }
}
