package backend.a105.jwt;

import lombok.Builder;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public class JwtProps {
    String issuer;
    String secret;
    @Getter
    private int expirySeconds;
    @Getter
    private int refreshExpirySeconds;

    @Builder
    protected JwtProps(String issuer, String secret, int expirySeconds, int refreshExpirySeconds) {
        this.issuer = issuer;
        this.secret = secret;
        this.expirySeconds = expirySeconds;
        this.refreshExpirySeconds = refreshExpirySeconds;
    }
}
