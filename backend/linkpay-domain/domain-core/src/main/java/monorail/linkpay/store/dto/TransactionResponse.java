package monorail.linkpay.store.dto;

import lombok.Builder;
import monorail.linkpay.store.domain.Store;

public record TransactionResponse(
        Data data,
        String signature) {

    @Builder
    public record Data(String storeId, long amount) {
        public static Data from(final Store store, long amount) {
            return Data.builder()
                    .storeId(store.getId().toString())
                    .amount(amount)
                    .build();
        }
    }
}
