package monorail.linkpay.settlement.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monorail.linkpay.common.domain.Point;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Table(name = "settlement")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE settlement SET deleted_at = CURRENT_TIMESTAMP WHERE settlement_id = ?")
@SQLRestriction("deleted_at is null")
@Entity
public class Settlement {

    @Id
    @Column(name = "settlement_id")
    private Long id;

    @Embedded
    private Point point;

    @Column(nullable = false)
    private Long storeId;

    @Builder
    private Settlement(final Long id, final Point point, final Long storeId) {
        this.id = id;
        this.point = point;
        this.storeId = storeId;
    }
}
