package monorail.linkpay.common.event;

import static monorail.linkpay.common.event.EventStatus.APPROVED;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.event.Event;
import monorail.linkpay.event.EventHandler;
import monorail.linkpay.event.EventType;
import monorail.linkpay.event.payload.BankResponseEventPayload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BankResponseEventHandler implements EventHandler<BankResponseEventPayload> {

    private final OutboxRepository outboxRepository;

    @Override
    public boolean supports(final Event<BankResponseEventPayload> event) {
        return EventType.BANK_RESPONSE.equals(event.type());
    }

    @Override
    public void handle(final Event<BankResponseEventPayload> event) {
        BankResponseEventPayload payload = event.payload();
        outboxRepository.updateOutboxStatusById(payload.outboxId(), APPROVED);
    }
}
