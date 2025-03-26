package monorail.linkpay.history.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.common.domain.TransactionType;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.linkedwallet.domain.LinkedMember;
import monorail.linkpay.wallet.domain.Wallet;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;
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

    @Column(nullable = true)
    private String merchantName;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "amount", nullable = false, updatable = false))
    private Point amount;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "remaining", nullable = false, updatable = false))
    private Point remaining;

    @Column(nullable = false, updatable = false)
    @Enumerated(STRING)
    private TransactionType transactionType;

    @Column(nullable = false, updatable = false)
    private LocalDateTime historyDate;

    @JoinColumn(name = "link_card_id", nullable = true)
    @ManyToOne(fetch = FetchType.LAZY)
    private LinkCard linkCard;

    @JoinColumn(name = "wallet_id", nullable = true)
    @ManyToOne(fetch = FetchType.LAZY)
    private Wallet wallet;

    @JoinColumn(name = "linked_member_id", nullable = true)
    @ManyToOne(fetch = FetchType.LAZY)
    private LinkedMember linkedMember;

    @Builder
    public WalletHistory(
            final Long id,
            final String merchantName,
            final Point amount,
            final TransactionType transactionType,
            final LocalDateTime historyDate,
            final Point remaining,
            final LinkCard linkCard,
            final Wallet wallet,
            final LinkedMember linkedMember) {
        this.id = id;
        this.merchantName = merchantName;
        this.amount = amount;
        this.transactionType = transactionType;
        this.historyDate = historyDate;
        this.remaining = remaining;
        this.linkCard = linkCard;
        this.wallet = wallet;
        this.linkedMember = linkedMember;
    }
}
