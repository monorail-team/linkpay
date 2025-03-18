package monorail.linkpay.linkcard.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monorail.linkpay.common.domain.BaseEntity;
import monorail.linkpay.linkedwallet.domain.LinkedWallet;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.wallet.domain.Wallet;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

@Table(name = "link_card")
@Getter
@EqualsAndHashCode(of = "linkCardId", callSuper = false)
@NoArgsConstructor(access = PROTECTED)
@Entity
public class LinkCard extends BaseEntity {

    @Id
    private Long linkCardId;

    @Column(nullable = true)
    private Long limitPrice;

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

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "linked_wallet_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private LinkedWallet linkedWallet;

    @JoinColumn(name = "wallet_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Wallet wallet;

    @Builder
    public LinkCard(
            final Long linkCardId,
            final Long limitPrice,
            final CardType cardType,
            final CardColor cardColor,
            final String cardName,
            final LocalDateTime deletedAt,
            final LocalDateTime expiredAt,
            final Member member,
            final LinkedWallet linkedWallet,
            final Wallet wallet
    ) {
        this.linkCardId = linkCardId;
        this.limitPrice = limitPrice;
        this.cardType = cardType;
        this.cardColor = cardColor;
        this.cardName = cardName;
        this.deletedAt = deletedAt;
        this.expiredAt = expiredAt;
        this.member = member;
        this.linkedWallet = linkedWallet;
        this.wallet = wallet;
    }
}
