package monorail.linkpay.payment.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.exception.ExceptionCode;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.token.TokenGenerator;
import monorail.linkpay.token.TokenType;
import monorail.linkpay.token.TokenValidationException;
import monorail.linkpay.token.TokenValidator;
import monorail.linkpay.token.dto.GeneratedToken;
import monorail.linkpay.token.dto.ValidatedToken;
import monorail.linkpay.util.json.JsonUtil;

import java.util.UUID;

@SupportLayer
@RequiredArgsConstructor
public class PaymentTokenProvider {

    private final TokenGenerator tokenGenerator;
    private final TokenValidator tokenValidator;

    public String generateFor(final Long memberId, final Long linkCardId) {
        PaymentTokenPayload payload = PaymentTokenPayload.builder()
                .memberId(memberId)
                .linkCardId(linkCardId)
                .nonce(UUID.randomUUID().toString())
                .build();

        GeneratedToken generated = tokenGenerator.generate(TokenType.PAYMENT, JsonUtil.toJson(payload));
        return generated.value();
    }

    public void validate(Long memberId, Long cardId, String paymentToken) {
        try {
            // todo 토큰 검증 리팩토링
            ValidatedToken validated = tokenValidator.validate(paymentToken);
            PaymentTokenPayload payload = JsonUtil.parse(validated.payload(), PaymentTokenPayload.class);
        } catch (TokenValidationException e) {
            throw new LinkPayException(ExceptionCode.FORBIDDEN_ACCESS, e.getMessage());
        }
    }

    @Builder
    public record PaymentTokenPayload(
            Long memberId,
            Long linkCardId,
            String nonce
    ) {
    }
}
