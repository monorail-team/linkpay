package monorail.linkpay.banking.account.eventhandler;

import monorail.linkpay.banking.common.event.Event;
import monorail.linkpay.banking.common.event.payload.EventPayload;

public interface EventHandler<T extends EventPayload> {

    boolean supports(Event<T> event);

    void handle(Event<T> event);
}
