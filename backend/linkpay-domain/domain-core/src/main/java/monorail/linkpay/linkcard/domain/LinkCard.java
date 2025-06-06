package monorail.linkpay.linkcard.domain;

import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;
import static monorail.linkpay.exception.ExceptionCode.INVALID_REQUEST;
import static monorail.linkpay.linkcard.domain.CardType.SHARED;

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
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monorail.linkpay.common.domain.BaseEntity;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.wallet.domain.Wallet;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Table(name = "link_card")
@Getter
@NoArgsConstructor(access = PROTECTED)
@SQLDelete(sql = "UPDATE link_card SET deleted_at = CURRENT_TIMESTAMP WHERE link_card_id = ?")
@SQLRestriction("deleted_at is null")
@Entity
public class LinkCard extends BaseEntity {

    @Id
    @Column(name = "link_card_id")
    private Long id;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "limit_price", nullable = false))
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
    @AttributeOverride(name = "amount", column = @Column(name = "usedPoint"))
    private Point usedPoint;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "wallet_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Wallet wallet;

    @Builder
    public LinkCard(
            final Long id,
            final Point limitPrice,
            final CardType cardType,
            final CardColor cardColor,
            final String cardName,
            final LocalDateTime expiredAt,
            final CardState state,
            final Point usedPoint,
            final Member member,
            final Wallet wallet
    ) {
        this.id = id;
        this.limitPrice = limitPrice;
        this.cardType = cardType;
        this.cardColor = cardColor;
        this.cardName = cardName;
        this.expiredAt = expiredAt;
        this.state = state;
        this.usedPoint = usedPoint;
        this.member = member;
        this.wallet = wallet;
    }

    public void usePoint(final Point point) {
        this.usedPoint = this.usedPoint.add(point);
    }

    public boolean isSharedCard() {
        return this.getCardType().equals(SHARED);
    }

    public void validateOwnership(final Long memberId) {
        if (!Objects.equals(memberId, this.member.getId())) {
            throw new LinkPayException(INVALID_REQUEST, "카드의 소유자가 아닙니다.");
        }
    }

    public void validateExpiredDate() {
        if (this.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new LinkPayException(INVALID_REQUEST, "만료된 링크카드입니다.");
        }
    }

    public void validateLimitPriceNotExceed(final Point point) {
        if (this.limitPrice.getAmount() < point.getAmount()) {
            throw new LinkPayException(INVALID_REQUEST, "사용금액이 한도를 초과했습니다.");
        }
    }

    public Long getWalletId() {
        return wallet.getId();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof LinkCard linkCard)) {
            return false;
        }
        return getId() != null && Objects.equals(getId(), linkCard.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
