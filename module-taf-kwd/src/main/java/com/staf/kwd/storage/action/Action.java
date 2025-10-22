package com.staf.kwd.storage.action;

@FunctionalInterface
public interface Action<V, T> {
    V execute(T t);
}
