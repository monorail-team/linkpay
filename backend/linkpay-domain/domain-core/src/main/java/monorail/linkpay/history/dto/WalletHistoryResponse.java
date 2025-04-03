package monorail.linkpay.history.dto;

import java.time.LocalDateTime;
import monorail.linkpay.history.domain.WalletHistory;

public record WalletHistoryResponse(
        String walletHistoryId,
        Long amount,
        Long remaining,
        String transactionType,
        LocalDateTime time,
        String linkCardId,
        String linkCardName
) {
    public static WalletHistoryResponse from(final WalletHistory walletHistory) {
        return new WalletHistoryResponse(
                walletHistory.getId().toString(),
                walletHistory.getAmount().getAmount(),
                walletHistory.getRemaining().getAmount(),
                walletHistory.getTransactionType().name(),
                walletHistory.getCreatedAt(),
                walletHistory.hasPayment() ? walletHistory.getPayment().getLinkCard().getId().toString() : null,
                walletHistory.hasPayment() ? walletHistory.getPayment().getLinkCard().getCardName() : null
        );
    }
}
