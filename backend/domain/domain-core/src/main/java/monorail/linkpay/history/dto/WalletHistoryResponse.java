package monorail.linkpay.history.dto;

import monorail.linkpay.history.domain.WalletHistory;

import java.time.LocalDateTime;

public record WalletHistoryResponse(
        Long id,
        Long amount,
        Long remaining,
        String transactionType,
        LocalDateTime time
) {
    public static WalletHistoryResponse from(final WalletHistory walletHistory) {
        return new WalletHistoryResponse(
                walletHistory.getId(),
                walletHistory.getAmount().getAmount(),
                walletHistory.getRemaining().getAmount(),
                walletHistory.getTransactionType().name(),
                walletHistory.getHistoryDate()
        );
    }
}
