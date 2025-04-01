package monorail.linkpay.payment.dto;

import lombok.Builder;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.store.domain.Store;

@Builder
public record TransactionInfo(Long storeId, Point point, String signature) {

    public TransactionInfo(Data data, String signature) {
        this(data.storeId(), data.point(), signature);
    }

    @Builder
    public record Data(Long storeId, Point point) {
        public static Data from(Store store, long amount) {
            return Data.builder()
                    .storeId(store.getId())
                    .point(new Point(amount))
                    .build();
        }
    }

    public Data data(){
        return Data.builder()
                .storeId(storeId)
                .point(point)
                .build();
    }
}
