package monorail.linkpay.wallet.domain;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monorail.linkpay.common.domain.BaseEntity;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.member.domain.Member;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Table(name = "wallet")
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = PROTECTED)
@SQLDelete(sql = "UPDATE users SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at is null")
@Entity
public class Wallet extends BaseEntity {

    @Id
    @Column(name = "wallet_id")
    private Long id;

    @Embedded
    private Point point;

    @JoinColumn(name = "member_id", nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder
    public Wallet(final Long id, final Member member) {
        this.id = id;
        this.point = new Point(0);
        this.member = member;
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
