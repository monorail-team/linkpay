package monorail.linkpay.banking.account.eventhandler;

import monorail.linkpay.event.Event;
import monorail.linkpay.event.payload.EventPayload;

public interface EventHandler<T extends EventPayload> {

    boolean supports(Event<T> event);

    void handle(Event<T> event);
}
