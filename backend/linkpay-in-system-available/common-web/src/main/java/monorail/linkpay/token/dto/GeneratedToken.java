package monorail.linkpay.token.dto;

import lombok.Builder;
import monorail.linkpay.token.TokenType;

@Builder
public record GeneratedToken(
        String tokenId,
        TokenType type,
        String value
) {
}
