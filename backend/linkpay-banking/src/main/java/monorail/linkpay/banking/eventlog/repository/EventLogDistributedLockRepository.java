package monorail.linkpay.banking.eventlog.repository;

import java.time.Duration;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EventLogDistributedLockRepository {

    private static final String KEY_FORMAT = "event-log::%s::lock";

    private final StringRedisTemplate redisTemplate;

    public boolean lock(final Long eventId, final Duration ttl) {
        final String key = generateKey(eventId);
        return Objects.requireNonNull(redisTemplate.opsForValue().setIfAbsent(key, "", ttl));
    }

    public void unlock(final Long eventId) {
        redisTemplate.delete(generateKey(eventId));
    }

    private String generateKey(final Long eventId) {
        return String.format(KEY_FORMAT, eventId);
    }
}
