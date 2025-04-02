package monorail.linkpay.linkcard.dto;

import java.time.LocalDate;
import monorail.linkpay.linkcard.domain.LinkCard;

public record LinkCardDetailResponse(
        String linkCardId,
        Long limitPrice,
        String cardType,
        String cardColor,
        String cardName,
        LocalDate expiredAt,
        Long usedPoint,
        String username,
        String state,
        String walletName
) {
    public static LinkCardDetailResponse from(LinkCard linkCard, String linkedWalletName) {
        return new LinkCardDetailResponse(
                linkCard.getId().toString(),
                linkCard.getLimitPrice().getAmount(),
                linkCard.getCardType().name(),
                linkCard.getCardColor().getHexCode(),
                linkCard.getCardName(),
                linkCard.getExpiredAt().toLocalDate().minusDays(1),
                linkCard.getUsedPoint().getAmount(),
                linkCard.getMember().getUsername(),
                linkCard.getState().name(),
                linkedWalletName
        );
    }
}
