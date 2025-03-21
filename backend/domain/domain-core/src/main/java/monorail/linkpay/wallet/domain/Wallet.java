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

    @Version
    private Long version;

    @Builder
    public Wallet(final Long id, final Point point, final Member member) {
        this.id = id;
        this.point = point;
        this.member = member;
    }

    public long getAmount() {
        return point.getAmount();
    }

    public void deductPoint(Point point) {
        this.point = this.point.subtract(point);
    }
}
