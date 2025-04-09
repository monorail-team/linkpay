package monorail.linkpay.store.dto;

import monorail.linkpay.store.domain.Store;

import java.util.ArrayList;
import java.util.List;

public record StoreListResponse(List<StoreInfo> stores) {

    public record StoreInfo(Long storeId, String storeName) {
    }

    public static StoreListResponse from(List<Store> storeList) {
        List<StoreInfo> result = new ArrayList<>();
        for (Store store : storeList) {
            result.add(new StoreInfo(store.getId(), store.getName()));
        }
        return new StoreListResponse(result);
    }
}
