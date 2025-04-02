package monorail.linkpay.payment.dto;

import lombok.Builder;

@Builder
public record PaymentInfo(Long memberId, Long linkCardId, String paymentToken) {
}
