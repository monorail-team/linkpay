package monorail.linkpay.common.domain.outbox;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monorail.linkpay.common.domain.BaseEntity;
import monorail.linkpay.event.EventType;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Table(name = "outbox")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE outbox SET deleted_at = CURRENT_TIMESTAMP WHERE outbox_id = ?")
@SQLRestriction("deleted_at is null")
@Entity
public class Outbox extends BaseEntity {

    @Id
    @Column(name = "outbox_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    private String payload;

    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus;

    @Builder
    private Outbox(final Long id, final EventType eventType, final String payload, final EventStatus eventStatus) {
        this.id = id;
        this.eventType = eventType;
        this.payload = payload;
        this.eventStatus = eventStatus;
    }

    public void approveEvent() {
        this.eventStatus = EventStatus.APPROVED;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Outbox outbox)) {
            return false;
        }
        return getId() != null && Objects.equals(getId(), outbox.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}