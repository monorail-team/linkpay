package monorail.linkpay.util.json;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToJsonConverter implements Converter<String, Json> {
    @Override
    public Json convert(String source) {
        return Json.of(source);
    }
}
