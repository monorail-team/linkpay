package monorail.linkpay.common.event;

import static lombok.AccessLevel.PRIVATE;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.event.Event;
import monorail.linkpay.event.payload.EventPayload;
import monorail.linkpay.util.json.Json;
import monorail.linkpay.util.json.JsonUtil;

@RequiredArgsConstructor(access = PRIVATE)
public final class OutboxEventConverter {

    public static Event<EventPayload> toEvent(final Outbox outbox) {
        return new Event<>(
                outbox.getId(),
                outbox.getEventType(),
                JsonUtil.parse(Json.of(outbox.getPayload()), outbox.getEventType().getPayloadClass())
        );
    }
}
