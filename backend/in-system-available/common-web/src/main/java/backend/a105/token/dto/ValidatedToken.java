package backend.a105.token.dto;

import backend.a105.token.TokenType;
import backend.a105.util.json.Json;
import lombok.Builder;

import java.time.Instant;

@Builder
public record ValidatedToken(String tokenId,
                             TokenType type,
                             Instant expiresAt,
                             Json payload) {
}