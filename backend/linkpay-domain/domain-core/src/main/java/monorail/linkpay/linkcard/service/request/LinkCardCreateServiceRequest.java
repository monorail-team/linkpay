package monorail.linkpay.linkcard.service.request;

import static monorail.linkpay.linkcard.domain.CardState.UNREGISTERED;
import static monorail.linkpay.linkcard.domain.CardType.OWNED;

import java.time.LocalDate;
import lombok.Builder;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.linkcard.domain.CardColor;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.wallet.domain.Wallet;

@Builder
public record LinkCardCreateServiceRequest(
        String cardName,
        Point limitPrice,
        LocalDate expiredAt
) {
    public LinkCard toLinkCard(final Long newId, final Wallet wallet, final Member member) {
        return LinkCard.builder()
                .id(newId)
                .cardColor(CardColor.getRandomColor())
                .cardName(this.cardName())
                .cardType(OWNED)
                .wallet(wallet)
                .limitPrice(this.limitPrice())
                .member(member)
                .expiredAt(this.expiredAt().plusDays(1).atStartOfDay())
                .usedPoint(new Point(0))
                .state(UNREGISTERED)
                .build();
    }
}
