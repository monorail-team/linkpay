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
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.common.domain.TransactionType;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.linkedwallet.domain.LinkedMember;
import monorail.linkpay.wallet.domain.Wallet;

@Table(name = "wallet_history")
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = PROTECTED)
@Entity
public class WalletHistory {

    @Id
    @Column(name = "wallet_history_id")
    private Long id;

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

    @JoinColumn(name = "link_card_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private LinkCard linkCard;

    @JoinColumn(name = "wallet_id")
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
