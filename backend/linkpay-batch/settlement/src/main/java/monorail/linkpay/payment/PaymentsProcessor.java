package monorail.linkpay.payment;

import static monorail.linkpay.common.event.EventStatus.PENDING;
import static monorail.linkpay.event.EventType.WITHDRAWAL;
import static monorail.linkpay.util.json.JsonUtil.toJson;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.common.event.Outbox;
import monorail.linkpay.event.payload.AccountWithdrawalEventPayload;
import monorail.linkpay.payment.domain.Payment;
import monorail.linkpay.util.id.IdGenerator;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class PaymentsProcessor {

    private final IdGenerator idGenerator;

    @Bean
    @StepScope
    public ItemProcessor<Payment, Outbox> paymentToOutboxProcessor() {
        return payment -> Outbox.builder()
                .id(idGenerator.generate())
                .eventType(WITHDRAWAL)
                .payload(toJson(toAccountWithdrawalEventPayload(payment)).value())
                .eventStatus(PENDING)
                .build();
    }

    private AccountWithdrawalEventPayload toAccountWithdrawalEventPayload(final Payment payment) {
        return new AccountWithdrawalEventPayload(payment.getWalletId(), payment.getAmount());
    }
}
