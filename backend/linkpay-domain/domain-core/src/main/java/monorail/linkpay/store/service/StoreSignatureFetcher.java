package monorail.linkpay.store.service;

import static monorail.linkpay.exception.ExceptionCode.NOT_FOUND_RESOURCE;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.store.domain.StoreSignature;
import monorail.linkpay.store.repository.StoreSignatureRepository;

@SupportLayer
@RequiredArgsConstructor
public class StoreSignatureFetcher {

    private final StoreSignatureRepository storeSignatureRepository;

    public StoreSignature fetchByStoreId(final Long storeId) {
        return storeSignatureRepository.findByStoreId(storeId)
                .orElseThrow(() -> new LinkPayException(NOT_FOUND_RESOURCE, "가게 아이디에 해당하는 서명 정보를 찾을 수 없습니다."));
    }
}
