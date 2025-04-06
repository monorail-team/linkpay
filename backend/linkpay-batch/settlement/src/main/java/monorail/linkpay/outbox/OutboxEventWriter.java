package monorail.linkpay.outbox;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.event.Event;
import monorail.linkpay.event.EventType.Topic;
import monorail.linkpay.event.payload.EventPayload;
import monorail.linkpay.util.json.JsonUtil;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
@RequiredArgsConstructor
public class OutboxEventWriter {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Bean
    @StepScope
    public ItemWriter<Event<? extends EventPayload>> outboxEventItemWriter() {
        return chunk -> chunk.forEach(this::sendEvent);
    }

    private void sendEvent(final Event<? extends EventPayload> event) {
        String topic = getTopic(event);
        String key = String.valueOf(event.eventId());
        String value = JsonUtil.toJson(event).value();

        kafkaTemplate.send(topic, key, value);
    }

    private String getTopic(final Event<?> event) {
        return switch (event.type()) {
            case DELETE -> Topic.ACCOUNT_DELETE;
            case DEPOSIT -> Topic.ACCOUNT_DEPOSIT;
            default -> Topic.ACCOUNT_WITHDRAWAL;
        };
    }
}
