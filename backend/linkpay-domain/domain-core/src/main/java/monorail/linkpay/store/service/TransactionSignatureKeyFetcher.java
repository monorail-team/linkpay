package monorail.linkpay.store.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.store.domain.TransactionSignatureKey;
import monorail.linkpay.store.repository.TransactionSignatureKeyRepository;

import static monorail.linkpay.exception.ExceptionCode.NOT_FOUND_RESOURCE;

@SupportLayer
@RequiredArgsConstructor
public class TransactionSignatureKeyFetcher {

    private final TransactionSignatureKeyRepository transactionSignatureKeyRepository;

    public TransactionSignatureKey fetchByStoreId(Long storeId) {
        return transactionSignatureKeyRepository.findByStoreId(storeId)
                .orElseThrow(() -> new LinkPayException(NOT_FOUND_RESOURCE, "가게 아이디에 해당하는 서명키를 찾을 수 없습니다."));
    }
}
