package monorail.linkpay.banking.common.event.producer;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.event.Event;
import monorail.linkpay.event.payload.EventPayload;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventProducer {

    private static final String BANKING_REPLY_EVENTS = "banking-reply";

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void publish(final Event<EventPayload> event) {
        kafkaTemplate.send(BANKING_REPLY_EVENTS, String.valueOf(event.eventId()));
    }
}
