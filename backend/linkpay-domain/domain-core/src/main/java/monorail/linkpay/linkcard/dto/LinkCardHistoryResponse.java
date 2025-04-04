package monorail.linkpay.linkcard.dto;

import java.time.LocalDateTime;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.payment.domain.Payment;

public record LinkCardHistoryResponse(
        String linkCardId,
        String linkCardName,
        Long usedPoint,
        String storeName,
        LocalDateTime time,
        String userName
) {

    public static LinkCardHistoryResponse from(final LinkCard linkCard, final Payment payment) {
        return new LinkCardHistoryResponse(
                linkCard.getId().toString(),
                linkCard.getCardName(),
                payment.getAmount().getAmount(),
                payment.getStore().getName(),
                payment.getCreatedAt(),
                linkCard.getMember().getUsername()
        );
    }
}
