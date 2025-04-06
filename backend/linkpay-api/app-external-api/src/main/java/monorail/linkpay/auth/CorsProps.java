package monorail.linkpay.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.Set;

@Setter
@Validated
@ConfigurationProperties(prefix = "cors.allow")
public class CorsProps {

    @NotNull
    Set<String> origins;

    @NotNull
    Set<String> methods;

    @NotNull
    Set<String> headers;
}
