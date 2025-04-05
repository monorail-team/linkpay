package monorail.linkpay.auth;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.Set;

@Setter
@Validated
@ConfigurationProperties(prefix = "cors.allow")
public class CorsProps {

    Set<String> origins;
    Set<String> methods;
    Set<String> headers;
}
