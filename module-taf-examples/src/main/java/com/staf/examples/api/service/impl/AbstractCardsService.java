package com.staf.examples.api.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.staf.examples.api.model.cards.ErrorBody;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

@RequiredArgsConstructor
public abstract class AbstractCardsService {

    @NonNull
    private final CloseableHttpClient client;

    @NonNull
    private final ObjectMapper objectMapper;

    public AbstractCardsService() {
        this(HttpClients.createDefault(), new ObjectMapper());
    }

    @NonNull
    protected abstract String baseUrl();

    protected static NameValuePair toPair(@NonNull final String name, @NonNull final Object value) {
        return new BasicNameValuePair(name, String.valueOf(value));
    }

    protected static List<NameValuePair> asParams(@NonNull final String name, @NonNull final Object value) {
        return Collections.singletonList(toPair(name, value));
    }

    protected static List<NameValuePair> asParams(final NameValuePair... pairs) {
        return Arrays.asList(pairs);
    }

    protected URI toUri(final String path) {
        return toUri(path, Collections.emptyList());
    }

    @SneakyThrows
    protected URI toUri(final String path, final List<NameValuePair> parameters) {
        return new URIBuilder()
                .setScheme("https")
                .setHost(baseUrl())
                .setPath(path)
                .setParameters(parameters)
                .build();
    }

    protected <T> Response<T> execute(@NonNull final URI uri, final Class<T> clazz) {
        return execute(new HttpGet(uri.normalize()), clazz);
    }

    @SneakyThrows
    protected <T> Response<T> execute(@NonNull final URI uri, @Nullable final Object body, final Class<T> clazz) {
        final HttpPost httpPost = new HttpPost(uri.normalize());
        final HttpEntity httpEntity = new ByteArrayEntity(objectMapper.writeValueAsBytes(body));
        httpPost.setEntity(httpEntity);
        return execute(httpPost, clazz);
    }

    @SneakyThrows
    protected <T> Response<T> execute(@NonNull final URI uri,
                                      final List<NameValuePair> parameters,
                                      final Class<T> clazz) {
        final HttpPost httpPost = new HttpPost(uri.normalize());
        final HttpEntity httpEntity = new UrlEncodedFormEntity(parameters);
        httpPost.setEntity(httpEntity);
        return execute(httpPost, clazz);
    }

    @SneakyThrows
    private <T> Response<T> execute(final HttpUriRequest request, final Class<T> clazz) {
        try (CloseableHttpResponse response = client.execute(request)) {
            if (!isSuccessful(response)) {
                throw buildServiceException(response);
            }
            return buildResponse(response, clazz);
        }
    }

    @SneakyThrows
    private ServiceException buildServiceException(@NonNull final HttpResponse response) {
        return ServiceException.builder()
                .code(response.getStatusLine().getStatusCode())
                .status(response.getStatusLine().getReasonPhrase())
                .protocol(response.getProtocolVersion().getProtocol())
                .headers(parseHeaders(response.headerIterator()))
                .errorBody(parseResponseBody(response, ErrorBody.class))
                .build();
    }

    private <T> Response<T> buildResponse(@NonNull final HttpResponse response, final Class<T> clazz) {
        return Response.<T>builder()
                .protocol(response.getProtocolVersion().getProtocol())
                .code(response.getStatusLine().getStatusCode())
                .status(response.getStatusLine().getReasonPhrase())
                .headers(parseHeaders(response.headerIterator()))
                .body(parseResponseBody(response, clazz))
                .build();
    }

    private Map<String, Object> parseHeaders(final HeaderIterator iterator) {
        final Map<String, Object> result = new HashMap<>();
        while (iterator.hasNext()) {
            final Header header = iterator.nextHeader();
            result.put(header.getName(), header.getValue());
        }
        return result;
    }

    @Nullable
    @SneakyThrows
    private <T> T parseResponseBody(final HttpResponse response, final Class<T> clazz) {
        final HttpEntity entity = response.getEntity();
        return objectMapper.readValue(entity.getContent(), clazz);
    }

    private static boolean isSuccessful(final HttpResponse response) {
        final int okStatusRangeStart = 200;
        final int okStatusRangeEnd = 299;
        final int code = response.getStatusLine().getStatusCode();
        return code >= okStatusRangeStart && code <= okStatusRangeEnd;
    }
}
