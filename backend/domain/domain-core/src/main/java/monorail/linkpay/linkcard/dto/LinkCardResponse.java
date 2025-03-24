package monorail.linkpay.linkcard.dto;

import jakarta.persistence.*;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.linkcard.domain.CardColor;
import monorail.linkpay.linkcard.domain.CardState;
import monorail.linkpay.linkcard.domain.CardType;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.member.domain.Member;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;

public record LinkCardResponse(
        Long id,
        Long limitPrice,
        String cardType,
        String cardColor,
        String cardName,
        LocalDateTime expiredAt,
        Long usedPoint
) {
    public static LinkCardResponse from(LinkCard linkCard) {
        return new LinkCardResponse(
                linkCard.getId(),
                linkCard.getLimitPrice().getAmount(),
                linkCard.getCardType().name(),
                linkCard.getCardColor().name(),
                linkCard.getCardName(),
                linkCard.getExpiredAt(),
                linkCard.getUsedPoint().getAmount()
        );
    }
}
