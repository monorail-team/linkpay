package monorail.linkpay.settlement.settlement.domain;

import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monorail.linkpay.common.domain.BaseEntity;
import monorail.linkpay.common.domain.Point;

@Table(name = "settlement")
@Entity
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = PROTECTED)
public class Settlement extends BaseEntity {

    @Id
    @Column(name = "settlement_id")
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
    public Settlement(
            final Long id,
            final Point amount,
            final SettlementStatus settlementStatus,
            final Long walletId,
            final Long storeId
    ) {
        this.id = id;
        this.amount = amount;
        this.settlementStatus = settlementStatus;
        this.walletId = walletId;
        this.storeId = storeId;
    }
}
