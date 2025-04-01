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

    private final SignatureProvider signatureProvider;
    private final StoreSignerFetcher storeSignerFetcher;

    @Transactional
    public TransactionInfo create(final Long storeId, final Point price) {
        // todo 가게 도메인 고도화 시 가게 정보 검증
        var storeSigner = storeSignerFetcher.fetchByStoreId(storeId);
        var txData = TransactionInfo.Data.builder()
                .storeId(storeId)
                .point(price)
                .build();
        var txSignature = signatureProvider.sign(txData, storeSigner.getEncryptKey());
        return TransactionInfo.from(txData, txSignature);
    }
}
