package monorail.linkpay.payment.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.payment.domain.Payment;
import monorail.linkpay.payment.repository.PaymentRepository;
import monorail.linkpay.util.id.IdGenerator;

@SupportLayer
@RequiredArgsConstructor
public class PaymentRecorder {

    private final PaymentRepository paymentRepository;
    private final IdGenerator idGenerator;

    public void record(LinkCard linkCard, Point amount, String merchantName) {
        paymentRepository.save(Payment.builder()
                .id(idGenerator.generate())
                .linkCard(linkCard)
                .member(linkCard.getMember())
                .amount(amount)
                .merchantName(merchantName)
                .historyDate(LocalDateTime.now())
                .build());
    }
}
