package monorail.linkpay.common.domain;

import static jakarta.persistence.EnumType.STRING;
import static monorail.linkpay.common.domain.BaseEntity.Status.DELETED;
import static monorail.linkpay.common.domain.BaseEntity.Status.USABLE;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseEntity {

    @Enumerated(STRING)
    @Column(nullable = false)
    protected Status status = USABLE;

    protected LocalDateTime deletedAt;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    protected LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    protected LocalDateTime modifiedAt;

    public enum Status {
        USABLE, DELETED
    }

    public void changeStatusToDeleted() {
        this.status = DELETED;
    }
}
