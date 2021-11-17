package de.lases.global.transport;

/**
 * A message (see {@code UIMessage}) can be an info, a warning, an error and
 * a fatal error.
 */
public enum MessageCategory {

    /**
     * An information message.
     */
    INFO,

    /**
     * A warning message.
     */
    WARNING,

    /**
     * An error message.
     */
    ERROR,

    /**
     * A fatal error message.
     */
    FATAL
}
