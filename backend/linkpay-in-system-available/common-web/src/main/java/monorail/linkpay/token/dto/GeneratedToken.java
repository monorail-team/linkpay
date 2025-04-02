package monorail.linkpay.token.dto;

import monorail.linkpay.token.TokenType;
import lombok.Builder;

@Builder
public record GeneratedToken(
        String tokenId,
        TokenType type,
        String value
) {
}
