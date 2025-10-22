package com.staf.api.core.serialization.impl;

import com.staf.api.core.serialization.ISerializationStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonSerializationStrategy implements ISerializationStrategy {
    private final Gson gson;

    public GsonSerializationStrategy() {
        this.gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
    }

    @Override
    public String serialize(final Object object) {
        return gson.toJson(object);
    }

    @Override
    public <T> T deserialize(final String jsonString, final Class<T> objectClass) {
        return gson.fromJson(jsonString, objectClass);
    }
}
