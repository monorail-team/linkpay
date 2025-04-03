package monorail.linkpay.settlement.outbox;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monorail.linkpay.event.EventType;

@Table(name = "outbox")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Outbox {

    @Id
    @Column(name = "outbox_id")
    private Long id;
    @Enumerated(EnumType.STRING)
    private EventType eventType;
    private String payload;
    private LocalDateTime createdAt;

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