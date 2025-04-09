package monorail.linkpay.wallethistory;

import static monorail.linkpay.common.event.EventStatus.PENDING;
import static monorail.linkpay.event.EventType.DEPOSIT;
import static monorail.linkpay.util.json.JsonUtil.toJson;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.common.event.Outbox;
import monorail.linkpay.event.payload.AccountDepositEventPayload;
import monorail.linkpay.history.domain.WalletHistory;
import monorail.linkpay.util.id.IdGenerator;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class WalletHistoryProcessor {

    private final IdGenerator idGenerator;

    @Bean
    @StepScope
    public ItemProcessor<WalletHistory, Outbox> walletHistoryToOutboxProcessor() {
        return walletHistory -> Outbox.builder()
                .id(idGenerator.generate())
                .eventType(DEPOSIT)
                .payload(toJson(toAccountDepositEventPayload(walletHistory)).value())
                .eventStatus(PENDING)
                .build();
    }

    private AccountDepositEventPayload toAccountDepositEventPayload(final WalletHistory walletHistory) {
        return new AccountDepositEventPayload(walletHistory.getWalletId(), walletHistory.getAmount());
    }
}
