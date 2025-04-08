package monorail.linkpay.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.payment.dto.PaymentInfo;
import monorail.linkpay.payment.dto.TransactionInfo;
import monorail.linkpay.store.dto.TransactionResponse;
import monorail.linkpay.util.encoder.FlatEncoder;

@Builder
public record PaymentsRequest(
        @NotNull String transactionFlat,
        @NotNull String linkCardId,
        @NotNull String paymentToken
) {

    public TransactionInfo txInfo() {
        TransactionResponse decoded = FlatEncoder.decode(transactionFlat, TransactionResponse.class);
        return TransactionInfo.builder()
                .storeId(Long.parseLong(decoded.storeId()))
                .point(new Point(decoded.amount()))
                .signature(decoded.transactionSignature())
                .build();
    }

    public PaymentInfo payInfo(Long memberId) {
        return PaymentInfo.builder()
                .memberId(memberId)
                .linkCardId(Long.parseLong(linkCardId))
                .paymentToken(paymentToken)
                .build();
    }
}