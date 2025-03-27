package monorail.linkpay.linkcard.dto;

import java.time.LocalDate;
import monorail.linkpay.linkcard.domain.LinkCard;

public record LinkCardResponse(
        String linkCardId,
        Long limitPrice,
        String cardType,
        String cardColor,
        String cardName,
        LocalDate expiredAt,
        Long usedPoint
) {
    public static LinkCardResponse from(final LinkCard linkCard) {
        return new LinkCardResponse(
                linkCard.getId().toString(),
                linkCard.getLimitPrice().getAmount(),
                linkCard.getCardType().name(),
                linkCard.getCardColor().getHexCode(),
                linkCard.getCardName(),
                linkCard.getExpiredAt().toLocalDate().minusDays(1),
                linkCard.getUsedPoint().getAmount()
        );
    }
}
