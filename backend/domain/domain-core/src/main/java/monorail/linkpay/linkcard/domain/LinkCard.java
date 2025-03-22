package monorail.linkpay.linkcard.domain;

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
import monorail.linkpay.common.domain.BaseEntity;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.linkedwallet.domain.LinkedWallet;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.wallet.domain.Wallet;

@Table(name = "link_card")
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = PROTECTED)
@Entity
public class LinkCard extends BaseEntity {

    @Id
    @Column(name = "link_card_id")
    private Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "limitPrice"))
    })
    private Point limitPrice;

    @Column(nullable = false, updatable = false)
    @Enumerated(STRING)
    private CardType cardType;

    @Column(nullable = false)
    @Enumerated(STRING)
    private CardColor cardColor;

    @Column(nullable = false)
    private String cardName;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    @Column(nullable = false)
    @Enumerated(STRING)
    private CardState state;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "usedPoint"))
    })
    private Point usedPoint;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "linked_wallet_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private LinkedWallet linkedWallet;

    @JoinColumn(name = "wallet_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Wallet wallet;

    @Builder
    public LinkCard(
            final Long id,
            final Point limitPrice,
            final CardType cardType,
            final CardColor cardColor,
            final String cardName,
            final LocalDateTime deletedAt,
            final LocalDateTime expiredAt,
            final Member member,
            final LinkedWallet linkedWallet,
            final Wallet wallet,
            final Point usedPoint,
            final CardState state
    ) {
        this.id = id;
        this.limitPrice = limitPrice;
        this.cardType = cardType;
        this.cardColor = cardColor;
        this.cardName = cardName;
        this.deletedAt = deletedAt;
        this.expiredAt = expiredAt;
        this.member = member;
        this.linkedWallet = linkedWallet;
        this.wallet = wallet;
        this.usedPoint = usedPoint;
        this.state = state;
    }
}
