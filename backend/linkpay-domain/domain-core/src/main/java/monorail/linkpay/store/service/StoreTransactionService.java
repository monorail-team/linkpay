package monorail.linkpay.store.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.store.dto.TransactionSign;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreTransactionService {
    private final TransactionSignatureProvider transactionSignatureProvider;
    private final StoreSignatureFetcher storeSignatureFetcher;

    @Transactional
    public TransactionSign create(final Long storeId, final long amount) {
        // todo 가게 및 거래 정보 검증
        var storeSignature = storeSignatureFetcher.fetchByStoreId(storeId);
        var data = TransactionSign.Data.from(storeSignature.getStore(), amount);
        var signature = transactionSignatureProvider.createSignature(data, storeSignature.getEncryptKey());
        return new TransactionSign(data, signature);
    }
}
