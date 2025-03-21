package monorail.linkpay.wallet.dto;

import java.util.List;

public record WalletHistoryListResponse(
        List<WalletHistoryResponse> walletHistories,
        Boolean hasNext
) {
}
