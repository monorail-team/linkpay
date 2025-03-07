package backend.a105.jwt;

import lombok.Builder;

@Builder
public record SignedJwt(String value, Long expirySeconds) {
}
