package de.lases.control.internal;

import jakarta.faces.context.ExceptionHandlerFactory;

/**
 * Factory class for the construction of a custom {@link UncheckedExceptionHandler}.
 */
public class UncheckedExceptionHandlerFactory extends ExceptionHandlerFactory {
    private final ExceptionHandlerFactory parent;

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
