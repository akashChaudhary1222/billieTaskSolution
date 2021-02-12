package main.java.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JsonUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);
    public static final ObjectMapper objectMapper = new ObjectMapper();

    public static String toJson(Object o) {
        if (o == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            LOGGER.error("Json Processing Error while serializing object: " + o, e);
        }
        return null;
    }

    public static <T> T fromJson(String json, Class<T> typeReference) {
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (IOException jpe) {
            LOGGER.error("Json Parsing Error. Malformed Json Content: [ " + json + " ]", jpe);
            jpe.printStackTrace();
            return null;
        }

    }
}

