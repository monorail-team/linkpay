package monorail.linkpay.token.dto;

import java.time.Instant;
import lombok.Builder;
import monorail.linkpay.token.TokenType;
import monorail.linkpay.util.json.Json;

@Builder
public record ValidatedToken(
        String tokenId,
        TokenType type,
        Instant expiresAt,
        Json payload
) {
}