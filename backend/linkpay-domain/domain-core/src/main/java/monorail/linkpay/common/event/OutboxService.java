package monorail.linkpay.common.event;

import static monorail.linkpay.exception.ExceptionCode.INVALID_EVENT_TYPE;

import java.util.List;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.event.Event;
import monorail.linkpay.event.EventHandler;
import monorail.linkpay.event.payload.EventPayload;
import monorail.linkpay.exception.LinkPayException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OutboxService {

    private final List<EventHandler> eventHandlers;

    public void handleEvent(final Event<EventPayload> event) {
        EventHandler<EventPayload> eventHandler = getEventHandler(event);
        eventHandler.handle(event);
    }

    private EventHandler<EventPayload> getEventHandler(final Event<EventPayload> event) {
        return eventHandlers.stream()
                .filter(eventHandler -> eventHandler.supports(event))
                .findAny()
                .orElseThrow(() -> new LinkPayException(INVALID_EVENT_TYPE, "지원하지 않는 이벤트 타입입니다."));
    }
}
