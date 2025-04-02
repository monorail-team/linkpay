package monorail.linkpay.banking.common.event.producer;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.banking.common.event.Event;
import monorail.linkpay.banking.common.event.payload.EventPayload;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void publish(final Event<EventPayload> event) {
        kafkaTemplate.send(event.type().getTopic(), event.eventId().toString());
    }
}
