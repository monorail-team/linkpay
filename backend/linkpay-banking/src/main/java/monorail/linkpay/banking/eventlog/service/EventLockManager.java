package monorail.linkpay.banking.eventlog.service;

import static monorail.linkpay.exception.ExceptionCode.SERVER_ERROR;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.banking.eventlog.repository.EventLogDistributedLockRepository;
import monorail.linkpay.exception.LinkPayException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;

@Slf4j
@SupportLayer
@RequiredArgsConstructor
public class EventLockManager {

    private static final Duration LOCK_TTL = Duration.ofSeconds(3);

    private final EventLogDistributedLockRepository eventLogDistributedLockRepository;

    @Retryable(
            maxAttempts = 4,
            backoff = @Backoff(delay = 500, multiplier = 2)
    )
    public void acquireLockWithRetry(final Long eventId) {
        if (!eventLogDistributedLockRepository.lock(eventId, LOCK_TTL)) {
            throw new LinkPayException(SERVER_ERROR, "해당 이벤트에 대한 락을 획득하지 못했습니다.");
        }
    }

    public void unlock(final Long eventId) {
        eventLogDistributedLockRepository.unlock(eventId);
    }

    @Recover
    public void recover(final LinkPayException ex, final Long eventId) {
        log.warn("재시도 최종 실패 이벤트 락 획득 불가 - {}", eventId);
        throw ex;
    }
}
