package monorail.linkpay.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Setter
@Validated
@ConfigurationProperties(prefix = "auth")
public class AuthProps {

    @NotBlank
    public String header;

    @NotBlank
    public String scheme;
}
