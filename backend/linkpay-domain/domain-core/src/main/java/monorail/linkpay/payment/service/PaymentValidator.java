package monorail.linkpay.payment.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.payment.dto.PaymentInfo;
import monorail.linkpay.store.domain.Store;
import monorail.linkpay.payment.dto.TransactionInfo;
import monorail.linkpay.store.service.StoreSignatureFetcher;
import monorail.linkpay.store.service.TransactionSignatureProvider;

@SupportLayer
@RequiredArgsConstructor
public class PaymentValidator {

    private final StoreSignatureFetcher storeSignatureFetcher;
    private final TransactionSignatureProvider signatureProvider;
    private final PaymentTokenProvider paymentTokenProvider;

    public void validate(final LinkCard linkCard,
                         final Store store,
                         final TransactionInfo txInfo,
                         final PaymentInfo payInfo) {
        validateTransaction(store, txInfo);
        validateToken(linkCard, payInfo.paymentToken());
        validateLinkCard(linkCard, txInfo.point(), payInfo.memberId());
    }

    private void validateTransaction(final Store store, final TransactionInfo txInfo) {
        var storeSignature = storeSignatureFetcher.fetchByStoreId(store.getId());
        signatureProvider.verifySignature(txInfo.data(), txInfo.signature(), storeSignature.getDecryptKey());
    }

    private void validateToken(final LinkCard linkCard, final String paymentToken) {
        paymentTokenProvider.validate(linkCard, paymentToken);
    }

    private void validateLinkCard(final LinkCard linkCard, final Point point, final Long memberId) {
        linkCard.validateExpiredDate();
        linkCard.validateLimitPriceNotExceed(point);
        linkCard.validateOwnership(memberId);
    }
}
