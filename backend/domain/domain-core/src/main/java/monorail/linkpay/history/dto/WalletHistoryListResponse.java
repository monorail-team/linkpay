package monorail.linkpay.history.dto;

import java.util.List;

public record WalletHistoryListResponse(
        List<WalletHistoryResponse> walletHistories,
        boolean hasNext
) {
}
