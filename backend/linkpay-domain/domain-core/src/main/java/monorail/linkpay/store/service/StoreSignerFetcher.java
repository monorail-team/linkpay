package monorail.linkpay.store.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.store.domain.StoreSigner;
import monorail.linkpay.store.repository.StoreSignerRepository;

import static monorail.linkpay.exception.ExceptionCode.NOT_FOUND_RESOURCE;

@SupportLayer
@RequiredArgsConstructor
public class StoreSignerFetcher {

    private final StoreSignerRepository storeSignerRepository;

    public StoreSigner fetchByStoreId(final Long storeId) {
        return storeSignerRepository.findByStoreId(storeId)
                .orElseThrow(() -> new LinkPayException(NOT_FOUND_RESOURCE, "가게 아이디에 해당하는 서명 정보를 찾을 수 없습니다."));
    }
}
