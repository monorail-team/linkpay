package monorail.linkpay.banking.common.event.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import monorail.linkpay.banking.account.service.AccountService;
import monorail.linkpay.banking.common.event.Event;
import monorail.linkpay.banking.common.event.EventType.Topic;
import monorail.linkpay.banking.common.event.payload.EventPayload;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountConsumer {

    private final AccountService accountService;

    @KafkaListener(topics = {
            Topic.ACCOUNT_DELETE,
            Topic.ACCOUNT_DEPOSIT,
            Topic.ACCOUNT_WITHDRAWAL
    }
            , groupId = "account")
    public void listen(final String messageJson, final Acknowledgment ack) {
        final Event<EventPayload> event = Event.fromJson(messageJson);
        try {
            accountService.handleEvent(event);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            ack.acknowledge();
        }
    }
}
