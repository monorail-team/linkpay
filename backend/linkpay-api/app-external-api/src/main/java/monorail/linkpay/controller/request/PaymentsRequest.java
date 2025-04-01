package monorail.linkpay.controller.request;

import jakarta.validation.constraints.Positive;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.payment.dto.PaymentInfo;
import monorail.linkpay.payment.dto.TransactionInfo;

public record PaymentsRequest(

        @Positive(message = "금액은 양수여야 합니다.")
        long amount,
        Long linkCardId,
        Long storeId,
        String transactionSignature,
        String paymentToken
) {
        public TransactionInfo txInfo() {
                return TransactionInfo.builder()
                        .storeId(storeId)
                        .point(new Point(amount))
                        .signature(transactionSignature)
                        .build();
        }

        public PaymentInfo payInfo(Long memberId) {
                return PaymentInfo.builder()
                        .memberId(memberId)
                        .linkCardId(linkCardId)
                        .paymentToken(paymentToken)
                        .build();
        }
}
