package monorail.linkpay.payment.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.history.domain.WalletHistory;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.linkcard.service.LinkCardUpdater;
import monorail.linkpay.payment.domain.Payment;
import monorail.linkpay.payment.repository.PaymentRepository;
import monorail.linkpay.store.domain.Store;
import monorail.linkpay.util.id.IdGenerator;
import org.springframework.transaction.annotation.Transactional;

@SupportLayer
@RequiredArgsConstructor
public class PaymentProcessor {

    private final IdGenerator idGenerator;
    private final PaymentRepository paymentRepository;
    private final LinkCardUpdater linkCardUpdater;

    @Transactional
    public Payment executePay(final Store store,
                              final LinkCard linkCard,
                              final Point point) {
        WalletHistory walletHistory = linkCardUpdater.useCard(linkCard, point);
        return paymentRepository.save(Payment.builder()
                .id(idGenerator.generate())
                .member(linkCard.getMember())
                .linkCard(linkCard)
                .store(store)
                .amount(point)
                .walletHistory(walletHistory)
                .build());
    }
}
