package de.lases.control.internal;

import jakarta.faces.context.ExceptionHandler;
import jakarta.faces.context.ExceptionHandlerFactory;

public class UncheckedExceptionHandlerFactory extends ExceptionHandlerFactory {
    private final ExceptionHandlerFactory parent;

    @SuppressWarnings("deprecation")
    public UncheckedExceptionHandlerFactory(ExceptionHandlerFactory parent) {
        this.parent = parent;
    }

    @Override
    public UncheckedExceptionHandler getExceptionHandler() {
        return new UncheckedExceptionHandler(parent.getExceptionHandler());
    }
}
