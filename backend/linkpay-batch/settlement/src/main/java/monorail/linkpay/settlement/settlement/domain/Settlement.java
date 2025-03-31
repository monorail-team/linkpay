package monorail.linkpay.settlement.settlement.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monorail.linkpay.settlement.common.domain.BaseEntity;
import monorail.linkpay.settlement.common.domain.Point;

import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

@Table(name = "settlement")
@Entity
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = PROTECTED)
public class Settlement extends BaseEntity {

    @Id
    private Long id;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "amount", nullable = false))
    private Point amount;

    @Column(nullable = false)
    @Enumerated(STRING)
    private SettlementStatus settlementStatus;

    private Long walletId;

    private Long storeId;

    @Builder
    public Settlement(final Long id,
                      final Point amount,
                      final SettlementStatus settlementStatus,
                      final Long walletId,
                      final Long storeId) {
        this.id = id;
        this.amount = amount;
        this.settlementStatus = settlementStatus;
        this.walletId = walletId;
        this.storeId = storeId;
    }
}
