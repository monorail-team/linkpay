package monorail.linkpay.outbox;

import static monorail.linkpay.common.domain.outbox.EventStatus.PENDING;
import static monorail.linkpay.event.EventType.WITHDRAWAL;
import static monorail.linkpay.util.json.JsonUtil.toJson;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.common.domain.outbox.Outbox;
import monorail.linkpay.common.domain.outbox.OutboxRepository;
import monorail.linkpay.event.payload.AccountDepositEventPayload;
import monorail.linkpay.event.payload.AccountWithdrawalEventPayload;
import monorail.linkpay.history.domain.WalletHistory;
import monorail.linkpay.payment.domain.Payment;
import monorail.linkpay.util.id.IdGenerator;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class OutboxWriter {

    private final OutboxRepository outboxRepository;
    private final IdGenerator idGenerator;

    @Bean
    @StepScope
    public ItemWriter<WalletHistory> depositHistoryOutboxItemWriter() {
        return chunk -> outboxRepository.saveAll(chunk.getItems().stream()
                .map(depositHistory -> Outbox.builder()
                        .id(idGenerator.generate())
                        .eventType(WITHDRAWAL)
                        .payload(toJson(toAccountDepositEventPayload(depositHistory)).value())
                        .eventStatus(PENDING)
                        .build()
                )
                .toList()
        );
    }

    @Bean
    @StepScope
    public ItemWriter<Payment> paymentOutboxItemWriter() {
        return chunk -> outboxRepository.saveAll(chunk.getItems().stream()
                .map(payment -> Outbox.builder()
                        .id(idGenerator.generate())
                        .eventType(WITHDRAWAL)
                        .payload(toJson(toAccountWithdrawalEventPayload(payment)).value())
                        .eventStatus(PENDING)
                        .build()
                )
                .toList()
        );
    }

    private AccountDepositEventPayload toAccountDepositEventPayload(final WalletHistory walletHistory) {
        return new AccountDepositEventPayload(walletHistory.getWalletId(), walletHistory.getAmount());
    }

    private AccountWithdrawalEventPayload toAccountWithdrawalEventPayload(final Payment payment) {
        return new AccountWithdrawalEventPayload(payment.getWalletId(), payment.getAmount());
    }
}
