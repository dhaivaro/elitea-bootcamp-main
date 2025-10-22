package com.staf.examples.demo.restassured;

import com.staf.api.config.RestConfiguration;
import com.staf.api.core.base.RestAssuredClient;
import com.staf.api.core.model.ApiRequestData;
import com.staf.api.core.model.Method;
import com.staf.common.metadata.ModuleType;
import com.staf.common.metadata.Toolset;
import com.staf.examples.api.model.CoinCap;
import io.restassured.builder.RequestSpecBuilder;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

@Slf4j
@Toolset({ModuleType.TESTNG, ModuleType.REST_ASSURED})
public class RestTest {

    private final String endpoint = RestConfiguration.get().baseUrl();

    @Test
    public void testAgnosticClientGet() {
        log.info("Endpoint: {}", endpoint);
        CoinCap response = RestAssuredClient.instance()
                .sendRequest(ApiRequestData.<CoinCap>builder()
                        .url(endpoint)
                        .className(CoinCap.class)
                        .httpMethod(Method.GET)
                        .build());
        Assertions.assertThat(response.getData().size())
                .as("Unable to receive any data from coincap")
                .isGreaterThan(0);
    }

    @Test
    public void testRestAssuredCore() {
        log.info("Endpoint: {}", endpoint);
        CoinCap response = RestAssuredClient.instance()
                .sendRequest(
                        new RequestSpecBuilder().setBaseUri(endpoint).build(),
                        io.restassured.http.Method.GET,
                        CoinCap.class);
        Assertions.assertThat(response.getData().size())
                .as("Unable to receive any data from coincap")
                .isGreaterThan(0);
    }
}
