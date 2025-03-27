package monorail.linkpay.linkcard.service.request;

import static monorail.linkpay.linkcard.domain.CardState.UNREGISTERED;
import static monorail.linkpay.linkcard.domain.CardType.SHARED;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.linkcard.domain.CardColor;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.wallet.domain.LinkedWallet;

@Builder
public record SharedLinkCardCreateServiceRequest(
        String cardName,
        Point limitPrice,
        LocalDate expiredAt,
        List<Long> memberIds,
        Long linkedWalletId
) {
    public LinkCard toLinkCard(final Long newId, final Member member, final LinkedWallet linkedwallet) {
        return LinkCard.builder()
                .id(newId)
                .cardColor(CardColor.getRandomColor())
                .cardName(cardName)
                .cardType(SHARED)
                .wallet(linkedwallet)
                .limitPrice(this.limitPrice())
                .member(member)
                .expiredAt(this.expiredAt().plusDays(1).atStartOfDay())
                .usedPoint(new Point(0))
                .state(UNREGISTERED)
                .build();
    }
}
