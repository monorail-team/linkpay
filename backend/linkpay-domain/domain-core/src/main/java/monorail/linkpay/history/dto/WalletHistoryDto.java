package monorail.linkpay.history.dto;

import java.time.LocalDateTime;
import monorail.linkpay.history.domain.WalletHistory;

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
                walletHistory.getAmount(),
                walletHistory.getRemaining().getAmount(),
                walletHistory.getTransactionType().name(),
                walletHistory.getCreatedAt());
    }
}
