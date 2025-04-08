package monorail.linkpay.store.dto;

import lombok.Builder;
import monorail.linkpay.payment.dto.TransactionInfo;
import monorail.linkpay.util.encoder.FlatEncoder;

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

    public Flat flat() {
        return new Flat(FlatEncoder.encode(this));
    }

    public record Flat(String data) {
    }
}
