package monorail.linkpay.store.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import monorail.linkpay.payment.dto.TransactionInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
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
