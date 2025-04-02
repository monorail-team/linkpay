package monorail.linkpay.banking.common.event;

import java.util.Objects;
import lombok.Getter;
import monorail.linkpay.banking.common.event.payload.EventPayload;
import monorail.linkpay.util.json.Json;
import monorail.linkpay.util.json.JsonUtil;

public record Event<T extends EventPayload>(Long eventId, EventType type, T payload) {

    public String toJson() {
        return JsonUtil.toJson(this).value();
    }

    public static Event<EventPayload> fromJson(final String json) {
        final EventRaw eventRaw = JsonUtil.parse(Json.of(json), EventRaw.class);
        if (Objects.isNull(eventRaw)) {
            return null;
        }
        EventType eventType = EventType.from(eventRaw.getType());

        return new Event<>(
                eventRaw.getEventId(),
                eventType,
                JsonUtil.parse(eventRaw.getPayload(), eventType.getPayloadClass())
        );
    }

    @Getter
    private static class EventRaw {
        private Long eventId;
        private String type;
        private Object payload;
    }
}
