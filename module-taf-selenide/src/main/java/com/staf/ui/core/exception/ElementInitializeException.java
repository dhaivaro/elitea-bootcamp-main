package com.staf.ui.core.exception;

public class ElementInitializeException extends RuntimeException {
    private static final long serialVersionUID = -9198792394795632667L;

    public ElementInitializeException() {
        super();
    }

    public ElementInitializeException(final String message) {
        super(message);
    }

    public ElementInitializeException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

    public ElementInitializeException(final String messagePattern, final String... messages) {
        super(String.format(messagePattern, messages));
    }
}
