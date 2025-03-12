package backend.a105.token.dto;

import backend.a105.token.TokenType;
import lombok.Builder;

@Builder
public record GeneratedToken(
        String tokenId,
        TokenType type,
        String token) {
}
