package monorail.linkpay.payment.service;

import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.token.TokenGenerator;
import monorail.linkpay.token.TokenType;
import monorail.linkpay.token.TokenValidator;
import monorail.linkpay.token.dto.GeneratedToken;
import monorail.linkpay.util.json.JsonUtil;

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

    public void validate(final LinkCard linkCard, final String paymentToken) {
        // todo 지문 인증 로직과 통합해서 구현 예정
//        try {
//            ValidatedToken validated = tokenValidator.validate(paymentToken);
//            PaymentTokenPayload payload = JsonUtil.parse(validated.payload(), PaymentTokenPayload.class);
//        } catch (TokenValidationException e) {
//            throw new LinkPayException(ExceptionCode.FORBIDDEN_ACCESS, e.getMessage());
//        }
    }

    @Builder
    public record PaymentTokenPayload(
            Long memberId,
            Long linkCardId,
            String nonce
    ) {
    }
}
