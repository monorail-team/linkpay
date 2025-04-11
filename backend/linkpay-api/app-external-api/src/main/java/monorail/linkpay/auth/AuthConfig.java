package monorail.linkpay.auth;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({AuthProps.class, CorsProps.class})
public class AuthConfig {
}
