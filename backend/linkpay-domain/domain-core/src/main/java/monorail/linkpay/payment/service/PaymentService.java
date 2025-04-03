package monorail.linkpay.payment.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.linkcard.service.LinkCardFetcher;
import monorail.linkpay.payment.domain.Payment;
import monorail.linkpay.payment.dto.PaymentDto;
import monorail.linkpay.payment.dto.PaymentInfo;
import monorail.linkpay.payment.dto.TransactionInfo;
import monorail.linkpay.store.domain.Store;
import monorail.linkpay.store.service.StoreFetcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final StoreFetcher storeFetcher;
    private final LinkCardFetcher linkCardFetcher;
    private final PaymentValidator paymentValidator;
    private final PaymentProcessor paymentProcessor;
    private final PaymentFetcher paymentFetcher;

    @Transactional
    public Long createPayment(final TransactionInfo txInfo, final PaymentInfo payInfo) {
        Store store = storeFetcher.fetchById(txInfo.storeId());
        LinkCard linkCard = linkCardFetcher.fetchByIdForUpdate(payInfo.linkCardId());

        paymentValidator.validate(linkCard, store, txInfo, payInfo);
        Payment payment = paymentProcessor.executePay(store, linkCard, txInfo.point());
        return payment.getId();
    }

    public PaymentDto readPayment(Long paymentId) {
        return PaymentDto.from(paymentFetcher.fetchById(paymentId));
    }

    public List<PaymentDto> readPaymentsByHistoryIdIn(Set<Long> walletHistoryIds) {
        return List.of();
    }
}
