package monorail.linkpay.fcm.client;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(FcmProps.class)
public class FcmConfig {
}
