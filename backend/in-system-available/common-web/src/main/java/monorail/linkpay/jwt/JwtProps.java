package monorail.linkpay.jwt;

import lombok.Builder;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public class JwtProps {

    String issuer;
    String secret;
    @Getter
    long expirySeconds;
    @Getter
    long refreshExpirySeconds;

    @Builder
    protected JwtProps(
            final String issuer,
            final String secret,
            final long expirySeconds,
            final long refreshExpirySeconds
    ) {
        this.issuer = issuer;
        this.secret = secret;
        this.expirySeconds = expirySeconds;
        this.refreshExpirySeconds = refreshExpirySeconds;
    }
}
