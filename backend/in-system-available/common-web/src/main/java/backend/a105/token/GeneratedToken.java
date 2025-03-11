package backend.a105.token;

import lombok.Builder;

@Builder
public record GeneratedToken(
        String tokenId,
        TokenType type,
        String token) {
}
