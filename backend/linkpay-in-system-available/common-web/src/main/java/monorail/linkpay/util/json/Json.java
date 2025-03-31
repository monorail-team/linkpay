package monorail.linkpay.util.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public record Json(String value) {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static Json of(final String value) {
        return new Json(value);
    }

    public static Json empty() {
        return new Json("");
    }

    public Json {
        try {
            mapper.readTree(value);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Json형식이 올바르지 않습니다");
        }
    }
}
