package backend.a105.jwt;

import lombok.Builder;

@Builder
public record GeneratedToken(
        String tokenId,
        TokenType type,
        String value) {
}
