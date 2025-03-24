package monorail.linkpay.wallet.dto;

import java.time.LocalDateTime;
import monorail.linkpay.wallet.domain.WalletHistory;

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
                walletHistory.getPoint().getAmount(),
                walletHistory.getRemaining().getAmount(),
                walletHistory.getTransactionType().name(),
                walletHistory.getCreatedAt()
        );
    }
}
