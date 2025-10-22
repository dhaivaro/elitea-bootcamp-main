package com.staf.api.util;

import com.staf.api.core.model.ApiRequestData;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import java.util.Objects;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RequestConverter {

    public static <T> RequestSpecification convert(final ApiRequestData<T> requestData) {
        final RequestSpecification specification = new RequestSpecBuilder()
                .setBaseUri(requestData.getUrl())
                .addHeaders(requestData.getHeaders())
                .build();
        if (Objects.nonNull(requestData.getBody())) {
            specification.body(requestData.getBody());
        }
        return specification;
    }
}
