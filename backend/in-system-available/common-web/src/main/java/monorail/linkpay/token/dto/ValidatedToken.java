package monorail.linkpay.token.dto;

import monorail.linkpay.token.TokenType;
import monorail.linkpay.util.json.Json;
import lombok.Builder;

import java.time.Instant;

@Builder
public record ValidatedToken(String tokenId,
                             TokenType type,
                             Instant expiresAt,
                             Json payload) {
}