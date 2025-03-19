package monorail.linkpay.wallet.service;

import java.time.LocalDateTime;

public record WalletHistoryResponse(Long id, Long amount, String transactionType, LocalDateTime time) {
}
