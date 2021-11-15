package de.uni_passau.fim.einfuehrung.einfuehrungsaufgabe_garstenauer_2.exception;

import jakarta.faces.context.ExceptionHandler;
import jakarta.faces.context.ExceptionHandlerFactory;

public class UncheckedExceptionHandlerFactory extends ExceptionHandlerFactory {
        private final ExceptionHandlerFactory parent;

        @SuppressWarnings("deprecation")
        public UncheckedExceptionHandlerFactory(ExceptionHandlerFactory parent) {
            this.parent = parent;
        }

        @Override
        public ExceptionHandler getExceptionHandler() {
            return new UncheckedExceptionHandler(parent.getExceptionHandler());
        }
    }
