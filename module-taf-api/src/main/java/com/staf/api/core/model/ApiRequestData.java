package com.staf.api.core.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Object describes request data for API request
 *
 * @param <T> deserialize response to this class
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ApiRequestData<T> {

    // if host is not null then url will be skipped (see getUrl())
    private String url;
    private String host;

    @Builder.Default
    private String path = "";

    private Class<T> className;
    private String body;

    @Builder.Default
    private Map<String, String> headers = new HashMap<>();

    private Method httpMethod;

    // if host is not null then url will be skipped
    public String getUrl() {
        return Objects.isNull(this.host) ? this.url : String.format("%s/%s", host, path);
    }
}
