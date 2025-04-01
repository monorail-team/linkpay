package monorail.linkpay.store.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.payment.dto.TransactionInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreTransactionService {

    private final TransactionSignatureProvider transactionSignatureProvider;
    private final StoreSignatureFetcher storeSignatureFetcher;

    @Transactional
    public TransactionInfo create(final Long storeId, final Point price) {
        // todo 가게 및 거래 정보 검증
        var storeSignature = storeSignatureFetcher.fetchByStoreId(storeId);
        var txData = TransactionInfo.Data.builder()
                .storeId(storeId)
                .point(price)
                .build();
        var txSignature = transactionSignatureProvider.createSignature(txData, storeSignature.getEncryptKey());
        return TransactionInfo.from(txData, txSignature);
    }
}
