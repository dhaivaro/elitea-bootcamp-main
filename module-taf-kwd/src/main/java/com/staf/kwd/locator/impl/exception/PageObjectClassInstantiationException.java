package com.staf.kwd.locator.impl.exception;

public class PageObjectClassInstantiationException extends RuntimeException {
    public PageObjectClassInstantiationException(final String message, final Throwable e) {
        super(message, e);
    }
}
