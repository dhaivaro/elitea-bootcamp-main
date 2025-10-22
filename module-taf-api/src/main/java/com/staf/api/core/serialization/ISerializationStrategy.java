package com.staf.api.core.serialization;

public interface ISerializationStrategy {
    String serialize(Object object);

    <T> T deserialize(String jsonString, Class<T> objectClass);
}
