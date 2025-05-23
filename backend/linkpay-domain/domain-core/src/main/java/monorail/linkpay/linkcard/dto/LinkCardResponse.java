package monorail.linkpay.linkcard.dto;

import java.time.LocalDate;
import monorail.linkpay.linkcard.domain.LinkCard;

public record LinkCardResponse(
        String linkCardId,
        String walletId,
        long walletAmount,
        Long limitPrice,
        String cardType,
        String cardColor,
        String cardName,
        LocalDate expiredAt,
        Long usedPoint,
        String username,
        String state
) {
    public static LinkCardResponse from(final LinkCard linkCard) {
        return new LinkCardResponse(
                linkCard.getId().toString(),
                linkCard.getWalletId().toString(),
                linkCard.getWallet().readAmount(),
                linkCard.getLimitPrice().getAmount(),
                linkCard.getCardType().name(),
                linkCard.getCardColor().getHexCode(),
                linkCard.getCardName(),
                linkCard.getExpiredAt().toLocalDate().minusDays(1),
                linkCard.getUsedPoint().getAmount(),
                linkCard.getMember().getUsername(),
                linkCard.getState().toString()
        );
    }
}
