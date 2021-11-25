package de.lases.control.internal;

import jakarta.faces.context.ExceptionHandlerFactory;

/**
 * Factory class for the construction of a custom {@link UncheckedExceptionHandler}.
 */
public class UncheckedExceptionHandlerFactory extends ExceptionHandlerFactory {
    private final ExceptionHandlerFactory parent;

    /**
     * Constructs an {@link UncheckedExceptionHandlerFactory}.
     * <p>
     * Do not call this method, it is used internally
     * as a factory and registered in the {@code faces-config.xml}
     * as such.
     * </p>
     * @param parent The parent {@link ExceptionHandlerFactory}.
     */
    @SuppressWarnings("deprecation")
    public UncheckedExceptionHandlerFactory(ExceptionHandlerFactory parent) {
        this.parent = parent;
    }

    /**
     * Gets an {@link UncheckedExceptionHandler}.
     *
     * @return The requested {@code UncheckedExceptionHandler}.
     */
    @Override
    public UncheckedExceptionHandler getExceptionHandler() {
        return new UncheckedExceptionHandler(parent.getExceptionHandler());
    }
}
