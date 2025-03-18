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
@EqualsAndHashCode(of = "walletId", callSuper = false)
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Wallet extends BaseEntity {

    @Id
    private Long walletId;

    @Embedded
    private Point point;

    @JoinColumn(name = "member_id", nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder
    public Wallet(final Long walletId, final Point point, final Member member) {
        this.walletId = walletId;
        this.point = point;
        this.member = member;
    }
}
