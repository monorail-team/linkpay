package monorail.linkpay.util.encoder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.util.json.Json;
import monorail.linkpay.util.json.JsonUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static monorail.linkpay.exception.ExceptionCode.CONVERSION_ERROR;
import static monorail.linkpay.exception.ExceptionCode.INVALID_REQUEST;

public class FlatEncoder {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String DELIMITER = "\\|";
    private static final String JOINER = "|";

    public static String encode(Object obj) {
        Json json = JsonUtil.toJson(obj);
        return jsonToFlatString(json);
    }

    public static <T> T decode(String flat, Class<T> clazz) {
        String jsonString = toJsonStringFromFlat(flat, clazz);
        return JsonUtil.parse(jsonString, clazz);
    }

    private static String jsonToFlatString(Json json) {
        try {
            JsonNode root = mapper.readTree(json.value());
            if (!root.isObject()) throw new LinkPayException(INVALID_REQUEST, "JSON은 객체여야 합니다.");

            List<String> tokens = new ArrayList<>();

            for (Iterator<Map.Entry<String, JsonNode>> it = root.fields(); it.hasNext(); ) {
                Map.Entry<String, JsonNode> entry = it.next();
                JsonNode value = entry.getValue();

                if (value.isValueNode()) {
                    tokens.add(value.asText());
                } else {
                    tokens.add(mapper.writeValueAsString(value));
                }
            }

            return String.join(JOINER, tokens);
        } catch (Exception e) {
            throw new LinkPayException(CONVERSION_ERROR, "JSON → Flat 변환 실패");
        }
    }

    private static <T> String toJsonStringFromFlat(String flat, Class<T> clazz) {
        try {
            String[] tokens = flat.split(DELIMITER, -1);
            Field[] fields = clazz.getDeclaredFields();

            if (tokens.length != fields.length) {
                throw new LinkPayException(CONVERSION_ERROR, "필드 수 불일치: tokens=" + tokens.length + ", fields=" + fields.length);
            }

            ObjectNode root = mapper.createObjectNode();

            for (int i = 0; i < fields.length; i++) {
                String fieldName = fields[i].getName();
                String rawValue = tokens[i];

                JsonNode valueNode = parseTokenToJsonNode(rawValue);
                root.set(fieldName, valueNode);
            }

            return mapper.writeValueAsString(root);
        } catch (Exception e) {
            throw new LinkPayException(CONVERSION_ERROR, "Flat → Json 변환 실패");
        }
    }

    private static JsonNode parseTokenToJsonNode(String token) {
        if (looksLikeJson(token)) {
            try {
                return mapper.readTree(token);
            } catch (Exception ignored) {
                // fall through
            }
        }
        return mapper.getNodeFactory().textNode(token);
    }

    private static boolean looksLikeJson(String s) {
        s = s.trim();
        return (s.startsWith("{") && s.endsWith("}")) ||
                (s.startsWith("[") && s.endsWith("]")) ||
                "true".equals(s) || "false".equals(s) || "null".equals(s) ||
                isNumeric(s);
    }

    private static boolean isNumeric(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }
}
