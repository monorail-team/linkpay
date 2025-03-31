package monorail.linkpay.store.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.store.domain.TransactionSignatureKey;
import monorail.linkpay.store.dto.TransactionResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionService {
    private final TransactionSignatureProvider transactionSignatureProvider;
    private final TransactionSignatureKeyFetcher transactionSignatureKeyFetcher;

    @Transactional
    public TransactionResponse create(final Long storeId, final long amount) {
        TransactionSignatureKey transactionSignatureKey = transactionSignatureKeyFetcher.fetchByStoreId(storeId);
        // todo 가게 및 거래 정보 검증
        var data = TransactionResponse.Data.from(transactionSignatureKey.getStore(), amount);
        var signature = transactionSignatureProvider.createSignature(data, transactionSignatureKey.getEncryptKey());
        return new TransactionResponse(data, signature);
    }
}
