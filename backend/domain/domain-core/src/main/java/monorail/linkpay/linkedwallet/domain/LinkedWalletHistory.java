package monorail.linkpay.linkedwallet.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.common.domain.TransactionType;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

@Table(name = "linked_wallet_history")
@Getter
@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode(of = "linkedWalletHistoryId", callSuper = false)
@Entity
public class LinkedWalletHistory {

    @Id
    private Long linkedWalletHistoryId;

    @Embedded
    private Point point;

    @Column(nullable = false, updatable = false)
    @Enumerated(STRING)
    private TransactionType transactionType;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @JoinColumn(name = "linked_wallet_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private LinkedWallet linkedWallet;

    @Builder
    public LinkedWalletHistory(
            final Long linkedWalletHistoryId,
            final Point point,
            final TransactionType transactionType,
            final LinkedWallet linkedWallet
    ) {
        this.linkedWalletHistoryId = linkedWalletHistoryId;
        this.point = point;
        this.transactionType = transactionType;
        this.createdAt = LocalDateTime.now();
        this.linkedWallet = linkedWallet;
    }
}
