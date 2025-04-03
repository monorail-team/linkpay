package monorail.linkpay.facade.dto;

import java.time.LocalDateTime;

import monorail.linkpay.history.dto.WalletHistoryDto;
import monorail.linkpay.payment.domain.Payment;
import monorail.linkpay.payment.dto.PaymentDto;

public record WalletHistoryResponse(
        String walletHistoryId,
        Long amount,
        Long remaining,
        String transactionType,
        LocalDateTime time,
        String linkCardId,
        String linkCardName
) {
    public static WalletHistoryResponse from(final WalletHistoryDto walletHistoryDto, final PaymentDto paymentDto) {
        return new WalletHistoryResponse(
                walletHistoryDto.walletHistoryId().toString(),
                walletHistoryDto.amount(),
                walletHistoryDto.remaining(),
                walletHistoryDto.transactionType(),
                walletHistoryDto.time(),
                paymentDto.linkCardId().toString(),
                paymentDto.linkCardName());
    }
}
