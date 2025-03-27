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
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monorail.linkpay.common.domain.BaseEntity;
import monorail.linkpay.common.domain.Point;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Table(name = "wallet")
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = PROTECTED)
@SQLDelete(sql = "UPDATE wallet SET deleted_at = CURRENT_TIMESTAMP WHERE wallet_id = ?")
@SQLRestriction("deleted_at is null")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "wallet_type")
@Entity
public abstract sealed class Wallet extends BaseEntity permits MyWallet, LinkedWallet {

    @Id
    @Column(name = "wallet_id")
    private Long id;

    @Embedded
    private Point point;

    @Builder
    public Wallet(final Long id) {
        this.id = id;
        this.point = new Point(0);
    }

    public long readAmount() {
        return point.getAmount();
    }

    public void chargePoint(Point point) {
        this.point = this.point.add(point);
    }

    public void deductPoint(Point point) {
        this.point = this.point.subtract(point);
    }
}
