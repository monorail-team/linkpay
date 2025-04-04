package monorail.linkpay.payment.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.exception.ExceptionCode;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.token.TokenGenerator;
import monorail.linkpay.token.TokenType;
import monorail.linkpay.token.TokenValidationException;
import monorail.linkpay.token.TokenValidator;
import monorail.linkpay.token.dto.GeneratedToken;
import monorail.linkpay.token.dto.ValidatedToken;
import monorail.linkpay.util.json.JsonUtil;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@SupportLayer
@RequiredArgsConstructor
public class PaymentTokenProvider {

    private final TokenGenerator tokenGenerator;
    private final TokenValidator tokenValidator;
    private final Set<String> usedNonces = ConcurrentHashMap.newKeySet(); // TODO: redis 사용

    public String generateFor(final Long memberId) {
        PaymentTokenPayload payload = PaymentTokenPayload.builder()
                .memberId(memberId)
                .nonce(UUID.randomUUID().toString())
                .build();

        GeneratedToken generated = tokenGenerator.generate(TokenType.PAYMENT, JsonUtil.toJson(payload));
        return generated.value();
    }

    public void validate(final LinkCard linkCard, final String paymentToken) {
        try {
            ValidatedToken validated = tokenValidator.validate(paymentToken);
            PaymentTokenPayload payload = JsonUtil.parse(validated.payload(), PaymentTokenPayload.class);

            if (!usedNonces.add(payload.nonce())) {
                throw new LinkPayException(ExceptionCode.FORBIDDEN_ACCESS, "해당 결제 토큰은 이미 사용되었습니다.");
            }

            if (!payload.memberId().equals(linkCard.getMember().getId())) {
                throw new LinkPayException(ExceptionCode.FORBIDDEN_ACCESS, "회원 정보가 일치하지 않습니다.");
            }

        } catch (TokenValidationException e) {
            throw new LinkPayException(ExceptionCode.FORBIDDEN_ACCESS, e.getMessage());
        }
    }

    @Builder
    public record PaymentTokenPayload(
            Long memberId,
            String nonce
    ) {
    }
}
