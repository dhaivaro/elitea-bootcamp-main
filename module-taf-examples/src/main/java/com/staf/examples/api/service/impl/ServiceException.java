package com.staf.examples.api.service.impl;

import com.staf.examples.api.model.cards.ErrorBody;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Builder
@Getter
@ToString
public final class ServiceException extends RuntimeException {
    private final String protocol;
    private final Integer code;
    private final String status;
    private final Map<String, Object> headers;
    private final ErrorBody errorBody;
}
