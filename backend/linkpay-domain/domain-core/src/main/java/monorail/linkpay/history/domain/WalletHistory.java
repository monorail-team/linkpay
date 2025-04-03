package monorail.linkpay.history.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monorail.linkpay.common.domain.BaseEntity;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.common.domain.TransactionType;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.wallet.domain.Wallet;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

@Table(name = "wallet_history")
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = PROTECTED)
@SQLDelete(sql = "UPDATE wallet_history SET deleted_at = CURRENT_TIMESTAMP WHERE wallet_history_id = ?")
@SQLRestriction("deleted_at is null")
@Entity
public class WalletHistory extends BaseEntity {

    @Id
    @Column(name = "wallet_history_id")
    private Long id;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "amount", nullable = false, updatable = false))
    private Point amount;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "remaining", nullable = false, updatable = false))
    private Point remaining;

    @Column(nullable = false, updatable = false)
    @Enumerated(STRING)
    private TransactionType transactionType;

    @JoinColumn(name = "wallet_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER) // todo: Lazy Loading 안되는 이슈 해결
    private Wallet wallet;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder
    private WalletHistory(
            final Long id,
            final Point amount,
            final Point remaining,
            final TransactionType transactionType,
            final Wallet wallet,
            final Member member
    ) {
        this.id = id;
        this.amount = amount;
        this.remaining = remaining;
        this.transactionType = transactionType;
        this.wallet = wallet;
        this.member = member;
    }
}
