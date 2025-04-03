package monorail.linkpay.banking.account.service;

import static monorail.linkpay.exception.ExceptionCode.INVALID_EVENT_TYPE;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import monorail.linkpay.banking.account.domain.Account;
import monorail.linkpay.banking.account.eventhandler.EventHandler;
import monorail.linkpay.banking.account.repository.AccountRepository;
import monorail.linkpay.banking.common.domain.Money;
import monorail.linkpay.banking.common.event.producer.EventProducer;
import monorail.linkpay.banking.eventlog.domain.EventLog;
import monorail.linkpay.banking.eventlog.repository.EventLogRepository;
import monorail.linkpay.banking.eventlog.service.EventLockManager;
import monorail.linkpay.banking.eventlog.service.EventLogFetcher;
import monorail.linkpay.event.Event;
import monorail.linkpay.event.payload.EventPayload;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.util.id.IdGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final EventLogRepository eventLogRepository;
    private final EventLogFetcher eventLogFetcher;
    private final List<EventHandler> eventHandlers;
    private final IdGenerator idGenerator;
    private final EventProducer eventProducer;
    private final EventLockManager eventLockManager;

    public void handleEvent(final Event<EventPayload> event) {
        eventLockManager.acquireLockWithRetry(event.eventId());
        try {
            eventLogFetcher.checkNotExistsById(event.eventId());
            EventHandler<EventPayload> eventHandler = getEventHandler(event);
            eventHandler.handle(event);
            eventLogRepository.save(EventLog.builder()
                    .id(event.eventId())
                    .eventType(event.type())
                    .build());
        } finally {
            eventLockManager.unlock(event.eventId());
            eventProducer.publish(event);
        }
    }

    private EventHandler<EventPayload> getEventHandler(final Event<EventPayload> event) {
        return eventHandlers.stream()
                .filter(eventHandler -> eventHandler.supports(event))
                .findAny()
                .orElseThrow(() -> new LinkPayException(INVALID_EVENT_TYPE, "지원하지 않는 이벤트 타입입니다."));
    }

    public Long createAccount(final Long walletId, final Long memberId) {
        return accountRepository.save(Account.builder()
                .id(idGenerator.generate())
                .walletId(walletId)
                .memberId(memberId)
                .money(new Money(0))
                .build()).getId();
    }
}
