package monorail.linkpay.banking.eventlog.domain;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monorail.linkpay.banking.common.event.EventType;

@Table(name = "event_log")
@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class EventLog {

    @Id
    @Column(name = "event_log_id")
    private Long id;

    @Column(nullable = false)
    private EventType eventType;

    @Builder
    private EventLog(final Long id, final EventType eventType) {
        this.id = id;
        this.eventType = eventType;
    }
}
