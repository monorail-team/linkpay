package monorail.linkpay.payment.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.payment.domain.Payment;
import monorail.linkpay.payment.repository.PaymentRepository;
import monorail.linkpay.store.domain.Store;
import monorail.linkpay.util.id.IdGenerator;
import org.springframework.transaction.annotation.Transactional;

@SupportLayer
@RequiredArgsConstructor
public class PaymentRecorder {

    private final IdGenerator idGenerator;
    private final PaymentRepository paymentRepository;

    @Transactional
    public void record(final Store store,
                       final LinkCard linkCard,
                       final Point point) {
        paymentRepository.save(Payment.builder()
                .id(idGenerator.generate())
                .member(linkCard.getMember())
                .linkCard(linkCard)
                .store(store)
                .amount(point)
                .build());
    }
}
