package monorail.linkpay.store.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.auth.service.TransactionSignatureProvider;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.store.domain.StoreSignature;
import monorail.linkpay.store.dto.TransactionResponse;
import monorail.linkpay.store.repository.StoreSignatureRepository;
import monorail.linkpay.util.json.JsonUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static monorail.linkpay.exception.ExceptionCode.NOT_FOUND_RESOURCE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionService {
    private final TransactionSignatureProvider transactionSignatureProvider;
    private final StoreSignatureRepository storeSignatureRepository;

    @Transactional
    public TransactionResponse create(final Long storeId, final long amount) {
        StoreSignature storeSignature = storeSignatureRepository.findByStoreId(storeId)
                .orElseThrow(() -> new LinkPayException(NOT_FOUND_RESOURCE, "가게 아이디에 해당하는 서명키를 찾을 수 없습니다."));
        // todo 가게 및 거래 정보 검증
        var data = TransactionResponse.Data.from(storeSignature.getStore(), amount);
        var signature = transactionSignatureProvider.createSignature(JsonUtil.toJson(data), storeSignature.getEncryptKey());
        return new TransactionResponse(data, signature);
    }
}
