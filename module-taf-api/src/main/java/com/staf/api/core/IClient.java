package com.staf.api.core;

import com.staf.api.core.model.ApiRequestData;

public interface IClient {

    <T> T sendRequest(ApiRequestData<T> apiRequestData);

    <T> void request(ApiRequestData<T> apiRequestData);
}
