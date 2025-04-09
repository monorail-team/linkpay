package monorail.linkpay.store.dto;

import lombok.Builder;
import monorail.linkpay.payment.dto.TransactionInfo;
import monorail.linkpay.util.encoder.FlatEncoder;

import java.nio.charset.StandardCharsets;

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
        Flat.Dto target = new Flat.Dto(Long.parseLong(storeId), amount, transactionSignature);
        String flatString = FlatEncoder.encode(target);
        System.out.println("flatString = " + flatString);
        Flat flat = new Flat(flatString.replace(FlatEncoder.DELIMITER, ""));
        byte[] bytes = flat.data.getBytes(StandardCharsets.UTF_8);
        System.out.println("flat.data = " + flat.data);
        System.out.println("📦 Length: " + bytes.length + " bytes");
        return flat;
    }



    public record Flat(String data) {
        private record Dto(long storeId, long amount, String signature) {
        }
    }
}
