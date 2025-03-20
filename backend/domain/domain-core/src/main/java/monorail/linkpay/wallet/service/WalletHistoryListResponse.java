package monorail.linkpay.wallet.service;

import java.util.List;

public record WalletHistoryListResponse(
        List<WalletHistoryResponse> walletHistories,
        Boolean hasNext
) {
}
