package monorail.linkpay.history.dto;

import monorail.linkpay.history.domain.WalletHistory;
import org.springframework.data.domain.Slice;

import java.util.List;

public record WalletHistoryListResponse(
        List<WalletHistoryResponse> walletHistories,
        boolean hasNext
) {
    public static WalletHistoryListResponse from(final Slice<WalletHistory> walletHistories) {
        return new WalletHistoryListResponse(
                walletHistories.stream().map(WalletHistoryResponse::from).toList(),
                walletHistories.hasNext());
    }
}
