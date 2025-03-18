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
@EqualsAndHashCode(of = "walletHistoryId", callSuper = false)
@NoArgsConstructor(access = PROTECTED)
@Entity
public class WalletHistory {

    @Id
    private Long walletHistoryId;

    @Embedded
    private Point point;

    @Column(nullable = false, updatable = false)
    private TransactionType transactionType;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @JoinColumn(name = "wallet_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Wallet wallet;

    @Builder
    public WalletHistory(
            final Long walletHistoryId,
            final Point point,
            final TransactionType transactionType,
            final LocalDateTime createdAt,
            final Wallet wallet
    ) {
        this.walletHistoryId = walletHistoryId;
        this.point = point;
        this.transactionType = transactionType;
        this.createdAt = LocalDateTime.now();
        this.wallet = wallet;
    }
}
