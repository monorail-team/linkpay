package monorail.linkpay.banking.common.event.producer;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.event.EventType.Topic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void publish(final String messageJson) {
        kafkaTemplate.send(Topic.BANK_RESPONSE, messageJson);
    }
}
