package backend.a105.jwt;

import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public class JwtProps {
    String issuer;
    String secret;
    long expirySeconds;
    long refreshExpirySeconds;

    @Builder
    protected JwtProps(String issuer, String secret, long expirySeconds, long refreshExpirySeconds) {
        this.issuer = issuer;
        this.secret = secret;
        this.expirySeconds = expirySeconds;
        this.refreshExpirySeconds = refreshExpirySeconds;
    }
}
