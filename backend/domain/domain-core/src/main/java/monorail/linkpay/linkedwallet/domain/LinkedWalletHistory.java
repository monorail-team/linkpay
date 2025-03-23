package monorail.linkpay.linkedwallet.domain;

import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
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

@Table(name = "linked_wallet_history")
@Getter
@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
@Entity
public class LinkedWalletHistory {

    @Id
    @Column(name = "linked_wallet_history_id")
    private Long id;

    @Embedded
    @AttributeOverrides(
            @AttributeOverride(name = "amount", column = @Column(name = "point"))
    )
    private Point point;

    @Embedded
    @AttributeOverrides(
            @AttributeOverride(name = "amount", column = @Column(name = "remaining"))
    )
    private Point remaining;

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
            final Long id,
            final Point point,
            final Point remaining,
            final TransactionType transactionType,
            final LinkedWallet linkedWallet
    ) {
        this.id = id;
        this.point = point;
        this.remaining = remaining;
        this.transactionType = transactionType;
        this.createdAt = LocalDateTime.now();
        this.linkedWallet = linkedWallet;
    }
}
