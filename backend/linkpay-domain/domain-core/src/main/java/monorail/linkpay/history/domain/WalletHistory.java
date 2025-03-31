package monorail.linkpay.history.domain;

import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monorail.linkpay.common.domain.BaseEntity;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.common.domain.TransactionType;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.wallet.domain.Wallet;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Table(name = "wallet_history")
@Getter
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
    @ManyToOne(fetch = FetchType.LAZY)
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

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof WalletHistory walletHistory)) {
            return false;
        }
        return id != null && Objects.equals(id, walletHistory.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
