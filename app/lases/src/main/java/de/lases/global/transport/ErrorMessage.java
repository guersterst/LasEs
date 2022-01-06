package de.lases.global.transport;

import java.io.Serial;
import java.io.Serializable;

public class ErrorMessage extends UIMessage implements Serializable {

    @Serial
    private static final long serialVersionUID = 12348923964729384L;

    private final String stackTrace;

    /**
     * Construct a UIMessage with a message text and a stack trace.
     *
     * @param message The message text.
     * @param stackTrace The stack trace.
     */
    public ErrorMessage(String message, String stackTrace) {
        super(message, MessageCategory.FATAL);
        this.stackTrace = stackTrace;
    }

    /**
     * Get stack trace. Is null if no stackTrace exists.
     *
     * @return Stack trace.
     */
    public String getStackTrace() {
        return stackTrace;
    }

}
