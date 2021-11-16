package de.lases.global.transport;

/**
 * Encapsulates information that is shown on the error page.
 */
public class ErrorMessage {

    private String errorMessage;

    private String stackTrace;

    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Add a custom error message that the user should see.
     *
     * @param errorMessage Error message that the user should see.
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    /**
     * Set the java stacktrace for the error.
     *
     * @param stackTrace Java stacktrace.
     */
    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }
}
