package monorail.linkpay.payment.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.linkcard.service.LinkCardFetcher;
import monorail.linkpay.linkcard.service.LinkCardUpdater;
import monorail.linkpay.payment.domain.Payment;
import monorail.linkpay.payment.dto.PaymentInfo;
import monorail.linkpay.payment.dto.TransactionInfo;
import monorail.linkpay.store.domain.Store;
import monorail.linkpay.store.service.StoreFetcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final StoreFetcher storeFetcher;
    private final LinkCardFetcher linkCardFetcher;
    private final LinkCardUpdater linkCardUpdater;
    private final PaymentValidator paymentValidator;
    private final PaymentRecorder paymentRecorder;

    @Transactional
    public void createPayment(final TransactionInfo txInfo, final PaymentInfo payInfo) {
        Store store = storeFetcher.fetchById(txInfo.storeId());
        LinkCard linkCard = linkCardFetcher.fetchByIdForUpdate(payInfo.linkCardId());

        paymentValidator.validate(linkCard, store, txInfo, payInfo);
        Payment payment = paymentRecorder.record(store, linkCard, txInfo.point());
        linkCardUpdater.pay(linkCard, txInfo.point(), payment);
    }
}
