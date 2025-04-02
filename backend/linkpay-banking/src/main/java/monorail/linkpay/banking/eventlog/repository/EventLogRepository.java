package monorail.linkpay.banking.eventlog.repository;

import monorail.linkpay.banking.eventlog.domain.EventLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventLogRepository extends JpaRepository<EventLog, Long> {
}
