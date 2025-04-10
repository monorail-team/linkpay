package monorail.linkpay.fcm.client;

import monorail.linkpay.util.json.Json;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "fcm")
public record FcmProps(
        String apiUrl,
        Json serviceAccountKey
) {
}
