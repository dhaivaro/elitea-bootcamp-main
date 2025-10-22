package com.staf.api.core.serialization.impl;

import com.staf.api.core.serialization.ISerializationStrategy;
import com.staf.common.exception.AqaException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JacksonSerializationStrategy implements ISerializationStrategy {
    private final ObjectMapper objectMapper;

    public JacksonSerializationStrategy() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public String serialize(final Object object) {
        try {
            return objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(object);
        } catch (Exception e) {
            log.error("Error occurred during serialization using Jackson: {}", e.getMessage());
            throw new AqaException(e.getMessage());
        }
    }

    @Override
    public <T> T deserialize(final String jsonString, final Class<T> objectClass) {
        try {
            return objectMapper.readValue(jsonString, objectClass);
        } catch (Exception e) {
            log.error("Error occurred during deserialization using Jackson: {}", e.getMessage());
            throw new AqaException(e.getMessage());
        }
    }
}
