package monorail.linkpay.store.dto;

import lombok.Builder;
import monorail.linkpay.payment.dto.TransactionInfo;

@Builder
public record TransactionResponse(
        String storeId,
        long amount,
        String transactionSignature) {
    public static TransactionResponse from(TransactionInfo txInfo) {
        return TransactionResponse.builder()
                .storeId(txInfo.storeId().toString())
                .amount(txInfo.point().getAmount())
                .transactionSignature(txInfo.signature())
                .build();
    }
}
