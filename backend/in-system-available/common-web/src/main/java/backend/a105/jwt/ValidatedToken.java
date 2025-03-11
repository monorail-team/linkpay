package backend.a105.jwt;

import lombok.Builder;

import java.time.Instant;
import java.util.Map;

@Builder
public record ValidatedToken(String tokenId,
                             TokenType type,
                             Instant expiresAt,
                             Map<String, Object> payload) {
}