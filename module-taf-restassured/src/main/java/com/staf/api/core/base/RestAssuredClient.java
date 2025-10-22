package com.staf.api.core.base;

import static io.restassured.RestAssured.given;

import com.staf.api.config.RestConfiguration;
import com.staf.api.core.IClient;
import com.staf.api.core.model.ApiRequestData;
import com.staf.api.util.RequestConverter;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Builder;

@Builder
public final class RestAssuredClient implements IClient {

    private static final RestConfiguration CONFIGURATION = RestConfiguration.get();

    // configuration should be done only once then it might be updated by user
    static {
        // put configuration related items here: enable/disable logging, etc.
        if (CONFIGURATION.isLoggingEnabled()) {
            RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        }
    }

    private RestAssuredClient() {}

    public static RestAssuredClient instance() {
        return new RestAssuredClient();
    }

    /**
     * Sends request per pre-defined request specification and ignoring any properties already defined in the class
     *
     * @param rq         built request specification
     * @param httpMethod http method
     * @return response
     */
    public Response request(final RequestSpecification rq, final Method httpMethod) {
        return given().spec(rq).request(httpMethod);
    }

    @Override
    public <T> void request(final ApiRequestData<T> apiRequestData) {
        request(
                RequestConverter.convert(apiRequestData),
                Method.valueOf(apiRequestData.getHttpMethod().toString()));
    }

    public <T> T sendRequest(final RequestSpecification rq, final Method httpMethod, final Class<T> clazz) {
        return request(rq, httpMethod).as(clazz);
    }

    public Response sendRequest(final String url, final Method httpMethod) {
        return request(new RequestSpecBuilder().setBaseUri(url).build(), httpMethod);
    }

    @Override
    public <T> T sendRequest(final ApiRequestData<T> apiRequestData) {
        // transform apiRequestData to RequestSpecification
        return sendRequest(
                RequestConverter.convert(apiRequestData),
                Method.valueOf(apiRequestData.getHttpMethod().toString()),
                apiRequestData.getClassName());
    }
}
