package monorail.linkpay.history.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.history.domain.Payment;
import monorail.linkpay.history.repository.PaymentRepository;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.util.id.IdGenerator;

import java.time.LocalDateTime;

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
