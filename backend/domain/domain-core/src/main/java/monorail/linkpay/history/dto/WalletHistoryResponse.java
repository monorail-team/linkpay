package monorail.linkpay.history.dto;

import java.time.LocalDateTime;
import monorail.linkpay.history.domain.WalletHistory;

public record WalletHistoryResponse(
        String id,
        Long amount,
        Long remaining,
        String transactionType,
        LocalDateTime time
) {
    public static WalletHistoryResponse from(final WalletHistory walletHistory) {
        return new WalletHistoryResponse(
                walletHistory.getId().toString(),
                walletHistory.getAmount().getAmount(),
                walletHistory.getRemaining().getAmount(),
                walletHistory.getTransactionType().name(),
                walletHistory.getCreatedAt()
        );
    }
}
