package backend.a105.util;

import backend.a105.type.Json;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // String -> Object 변환
    public static <T> T parse(Json json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json.value(), clazz);
        } catch (JsonMappingException e) {
            throw new IllegalArgumentException("JSON 매핑 실패: " + e.getMessage(), e);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("JSON 파싱 실패: " + e.getMessage(), e);
        }
    }

    // Object -> String 변환
    public static Json toJson(Object o) {
        try {
            return Json.of(objectMapper.writeValueAsString(o));
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Object -> JSON 변환 실패: " + e.getMessage(), e);
        }
    }
}

