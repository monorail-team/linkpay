package monorail.linkpay.wallet.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monorail.linkpay.common.domain.BaseEntity;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.member.domain.Member;

import static lombok.AccessLevel.PROTECTED;

@Table(name = "wallet")
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = PROTECTED)
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
