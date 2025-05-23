package monorail.linkpay.util.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // String -> Object 변환
    public static <T> T parse(final String jsonString, final Class<T> clazz) {
        return parse(Json.of(jsonString), clazz);
    }

    // Json -> Object 변환
    public static <T> T parse(final Json json, final Class<T> clazz) {
        try {
            return objectMapper.readValue(json.value(), clazz);
        } catch (JsonMappingException e) {
            throw new IllegalArgumentException("JSON 매핑 실패: " + e.getMessage(), e);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("JSON 파싱 실패: " + e.getMessage(), e);
        }
    }


    public static <T> T parse(final Object data, final Class<T> clazz) {
        return objectMapper.convertValue(data, clazz);
    }


    // Object -> Json 변환
    public static Json toJson(final Object o) {
        try {
            return Json.of(objectMapper.writeValueAsString(o));
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Object -> JSON 변환 실패: " + e.getMessage(), e);
        }
    }
}

