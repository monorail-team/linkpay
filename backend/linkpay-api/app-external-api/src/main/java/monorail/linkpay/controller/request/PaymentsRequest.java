package monorail.linkpay.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.payment.dto.PaymentInfo;
import monorail.linkpay.payment.dto.TransactionInfo;
import monorail.linkpay.util.encoder.Base85Encoder;
import monorail.linkpay.util.encoder.FlatEncoder;

@Builder
public record PaymentsRequest(
        @NotNull String transactionFlat,
        @NotNull String linkCardId,
        @NotNull String paymentToken
) {

    public TransactionInfo txInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append(transactionFlat);
        sb.insert(20, FlatEncoder.DELIMITER);
        sb.insert(10, FlatEncoder.DELIMITER);
        String[] split = sb.toString().split(FlatEncoder.DELIMITER);
        return TransactionInfo.builder()
                .storeId(Base85Encoder.decodeBase85ToLong(split[0]))
                .point(new Point(Base85Encoder.decodeBase85ToLong(split[1])))
                .signature(split[2])
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