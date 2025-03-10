package backend.a105.jwt;

import lombok.Builder;

@Builder
public record GeneratedToken(
        String tokenId,
        JwtType type,
        String value) {
}
