package monorail.linkpay.wallet.dto;

import java.time.LocalDateTime;

public record WalletHistoryResponse(
        Long id,
        Long amount,
        Long remaining,
        String transactionType,
        LocalDateTime time
) {
}
