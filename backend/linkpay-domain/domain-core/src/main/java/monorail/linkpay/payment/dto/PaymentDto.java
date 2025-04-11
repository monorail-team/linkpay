package monorail.linkpay.payment.dto;

import lombok.Builder;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.payment.domain.Payment;

@Builder
public record PaymentDto(Long paymentId,
                         Long linkCardId,
                         String linkCardName,
                         Long walletHistoryId
                         ) {
    public static PaymentDto from(Payment payment) {
        LinkCard linkCard = payment.getLinkCard();
        return PaymentDto.builder()
                .paymentId(payment.getId())
                .linkCardId(linkCard.getId())
                .linkCardName(linkCard.getCardName())
                .walletHistoryId(payment.getWalletHistory().getId())
                .build();
    }
}
