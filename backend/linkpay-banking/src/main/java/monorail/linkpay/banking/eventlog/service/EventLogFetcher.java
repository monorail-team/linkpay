package monorail.linkpay.banking.eventlog.service;

import static monorail.linkpay.exception.ExceptionCode.INVALID_REQUEST;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.banking.eventlog.repository.EventLogRepository;
import monorail.linkpay.exception.LinkPayException;

@SupportLayer
@RequiredArgsConstructor
public class EventLogFetcher {

    private final EventLogRepository eventLogRepository;

    public void checkNotExistsById(final Long id) {
        if (eventLogRepository.existsById(id)) {
            throw new LinkPayException(INVALID_REQUEST, "이미 처리된 이벤트입니다.");
        }
    }
}
