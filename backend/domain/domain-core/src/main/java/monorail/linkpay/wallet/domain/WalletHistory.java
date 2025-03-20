package monorail.linkpay.wallet.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.common.domain.TransactionType;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

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
    private Point point;

    @Embedded
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
