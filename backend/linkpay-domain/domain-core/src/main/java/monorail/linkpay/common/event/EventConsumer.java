package monorail.linkpay.common.event;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import monorail.linkpay.event.Event;
import monorail.linkpay.event.EventType.Topic;
import monorail.linkpay.event.payload.EventPayload;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventConsumer {

    private final OutboxService outboxService;

    @KafkaListener(topics = Topic.BANK_RESPONSE, groupId = "account")
    public void listen(final String messageJson, final Acknowledgment ack) {
        final Event<EventPayload> event = Event.fromJson(messageJson);
        if (Objects.nonNull(event)) {
            outboxService.handleEvent(event);
        }
        ack.acknowledge();
    }
}
