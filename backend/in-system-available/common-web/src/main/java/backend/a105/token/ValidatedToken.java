package backend.a105.token;

import backend.a105.type.Json;
import lombok.Builder;

import java.time.Instant;
import java.util.Map;

@Builder
public record ValidatedToken(String tokenId,
                             TokenType type,
                             Instant expiresAt,
                             Json payload) {
}