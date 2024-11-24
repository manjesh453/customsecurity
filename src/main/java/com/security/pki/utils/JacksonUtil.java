package com.security.pki.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;

import java.io.IOException;

@NoArgsConstructor
public class JacksonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> T get(String content, Class clazz) throws IOException {
        return (T) objectMapper.readValue(content, clazz);
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
