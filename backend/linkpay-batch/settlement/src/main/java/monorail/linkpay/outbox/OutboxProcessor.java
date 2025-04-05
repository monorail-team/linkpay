package monorail.linkpay.outbox;

import monorail.linkpay.common.domain.outbox.Outbox;
import monorail.linkpay.common.domain.outbox.OutboxEventConverter;
import monorail.linkpay.event.Event;
import monorail.linkpay.event.payload.EventPayload;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OutboxProcessor {

    @Bean
    @StepScope
    public ItemProcessor<Outbox, Event<EventPayload>> outboxProcessor() {
        return OutboxEventConverter::toEvent;
    }
}
