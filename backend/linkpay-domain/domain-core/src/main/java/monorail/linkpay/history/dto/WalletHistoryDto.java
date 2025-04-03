package monorail.linkpay.history.dto;

import monorail.linkpay.history.domain.WalletHistory;
import monorail.linkpay.payment.domain.Payment;

import java.time.LocalDateTime;

public record WalletHistoryDto(
        Long walletHistoryId,
        Long amount,
        Long remaining,
        String transactionType,
        LocalDateTime time
) {
    public static WalletHistoryDto from(final WalletHistory walletHistory) {
        return new WalletHistoryDto(
                walletHistory.getId(),
                walletHistory.getAmount().getAmount(),
                walletHistory.getRemaining().getAmount(),
                walletHistory.getTransactionType().name(),
                walletHistory.getCreatedAt());
    }
}
