package monorail.linkpay.facade.dto;

import monorail.linkpay.history.dto.WalletHistoryDto;
import monorail.linkpay.payment.dto.PaymentDto;

import java.time.LocalDateTime;

import static java.util.Objects.isNull;

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
                isNull(paymentDto) ? null : paymentDto.linkCardId().toString(),
                isNull(paymentDto) ? null : paymentDto.linkCardName());
    }
}
