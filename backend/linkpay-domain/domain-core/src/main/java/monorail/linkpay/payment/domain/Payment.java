package monorail.linkpay.payment.domain;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monorail.linkpay.common.domain.BaseEntity;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.history.domain.WalletHistory;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.store.domain.Store;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Table(name = "payment")
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = PROTECTED)
@SQLDelete(sql = "UPDATE payment SET deleted_at = CURRENT_TIMESTAMP WHERE payment_id = ?")
@SQLRestriction("deleted_at is null")
@Entity
public class Payment extends BaseEntity {

    @Id
    @Column(name = "payment_id")
    private Long id;

    @JoinColumn(name = "link_card_id", nullable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private LinkCard linkCard;

    @JoinColumn(name = "member_id", nullable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "amount", nullable = false, updatable = false))
    private Point amount;

    @JoinColumn(name = "store_id", nullable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;

    @JoinColumn(name = "wallet_history_id", nullable = false, updatable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private WalletHistory walletHistory;

    @Builder
    private Payment(
            final Long id,
            final LinkCard linkCard,
            final Member member,
            final Point amount,
            final Store store,
            final WalletHistory walletHistory
    ) {
        this.id = id;
        this.linkCard = linkCard;
        this.member = member;
        this.amount = amount;
        this.store = store;
        this.walletHistory = walletHistory;
    }
}
