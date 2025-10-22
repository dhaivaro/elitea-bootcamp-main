package com.staf.examples.api.service.impl;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@Builder
@ToString
public final class Response<T> {

    private final String protocol;
    private final Integer code;
    private final String status;
    private final Map<String, Object> headers;
    private final T body;
}
