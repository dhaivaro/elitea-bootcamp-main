package com.staf.common.exception;

public class AqaException extends RuntimeException {
    public AqaException() {
        super();
    }

    public AqaException(final String message) {
        super(message);
    }

    public AqaException(final String messagePattern, final String... messages) {
        super(String.format(messagePattern, messages));
    }
}
