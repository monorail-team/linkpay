package monorail.linkpay.wallet.domain;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monorail.linkpay.common.domain.BaseEntity;
import monorail.linkpay.common.domain.Point;

@Table(name = "wallet")
@Getter
@NoArgsConstructor(access = PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "wallet_type")
@Entity
public abstract class Wallet extends BaseEntity {

    @Id
    @Column(name = "wallet_id")
    private Long id;

    @Embedded
    private Point point;

    public Wallet(final Long id) {
        this.id = id;
        this.point = new Point(0);
    }

    public long readAmount() {
        return point.getAmount();
    }

    public void chargePoint(final Point point) {
        this.point = this.point.add(point);
    }

    public void deductPoint(final Point point) {
        this.point = this.point.subtract(point);
    }

}
