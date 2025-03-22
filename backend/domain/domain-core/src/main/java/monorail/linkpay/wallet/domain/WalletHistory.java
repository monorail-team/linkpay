package monorail.linkpay.wallet.domain;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.common.domain.TransactionType;

@Table(name = "wallet_history")
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = PROTECTED)
@Entity
public class WalletHistory {

    @Id
    @Column(name = "wallet_history_id")
    private Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "point"))
    })
    private Point point;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "remaining"))
    })
    private Point remaining;

    @Column(nullable = false, updatable = false)
    private TransactionType transactionType;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @JoinColumn(name = "wallet_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Wallet wallet;

    @Builder
    public WalletHistory(
            final Long id,
            final Point point,
            final Point remaining,
            final TransactionType transactionType,
            final LocalDateTime createdAt,
            final Wallet wallet
    ) {
        this.id = id;
        this.point = point;
        this.remaining = remaining;
        this.transactionType = transactionType;
        this.createdAt = LocalDateTime.now();
        this.wallet = wallet;
    }
}
