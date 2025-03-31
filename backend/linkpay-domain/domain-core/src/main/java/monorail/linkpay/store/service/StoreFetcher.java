package monorail.linkpay.store.service;

import static monorail.linkpay.exception.ExceptionCode.NOT_FOUND_RESOURCE;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.store.domain.Store;
import monorail.linkpay.store.repository.StoreRepository;

@SupportLayer
@RequiredArgsConstructor
public class StoreFetcher {

    private final StoreRepository storeRepository;

    public Store fetchById(final Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new LinkPayException(NOT_FOUND_RESOURCE, "가게 아이디에 해당하는 가게를 찾을 수 없습니다."));
    }
}
