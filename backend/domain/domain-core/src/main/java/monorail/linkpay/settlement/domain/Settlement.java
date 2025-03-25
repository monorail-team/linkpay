package monorail.linkpay.settlement.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.linkedwallet.domain.LinkedMember;
import monorail.linkpay.wallet.domain.Wallet;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

@Table(name = "settlement")
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Settlement {

    @Id
    @Column(name = "settle_id")
    private Long id;

    @Column(nullable = true)
    private String merchantName;

    @Embedded
    @AttributeOverrides(
            @AttributeOverride(name = "amount", column = @Column(name = "amount", nullable = false))
    )
    private Point amount;

    @Embedded
    @AttributeOverrides(
            @AttributeOverride(name = "amount", column = @Column(name = "remaining", nullable = false))
    )
    private Point remaining;

    @Column(nullable = false, updatable = false)
    @Enumerated(STRING)
    private SettleType settleType;

    @Column(nullable = false, updatable = false)
    @Enumerated(STRING)
    private SettleStatus settleStatus;

    @Column(nullable = false, updatable = false)
    private LocalDateTime settleDate;

    @JoinColumn(name = "link_card_id", nullable = true)
    @ManyToOne(fetch = FetchType.LAZY)
    private LinkCard linkCard;

    @JoinColumn(name = "wallet_id", nullable = true)
    @ManyToOne(fetch = FetchType.LAZY)
    private Wallet wallet;

    @JoinColumn(name = "linked_member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private LinkedMember linkedMember;

    @Builder
    public Settlement(
            final Long id,
            final String merchantName,
            final Point amount,
            final SettleType settleType,
            final LocalDateTime settleDate,
            final SettleStatus settleStatus,
            final Point remaining,
            final LinkCard linkCard,
            final Wallet wallet,
            final LinkedMember linkedMember) {
        this.id = id;
        this.merchantName = merchantName;
        this.amount = amount;
        this.settleType = settleType;
        this.settleDate = settleDate;
        this.settleStatus = settleStatus;
        this.remaining = remaining;
        this.linkCard = linkCard;
        this.wallet = wallet;
        this.linkedMember = linkedMember;
    }
}
