package monorail.linkpay.history.dto;

import monorail.linkpay.history.domain.WalletHistory;
import org.springframework.data.domain.Slice;

import java.util.List;

public record WalletHistoryListDto(
        List<WalletHistoryDto> walletHistoryDtos,
        boolean hasNext
) {
    public static WalletHistoryListDto from(final Slice<WalletHistory> walletHistories) {
        return new WalletHistoryListDto(
                walletHistories.stream().map(WalletHistoryDto::from).toList(),
                walletHistories.hasNext());
    }
}
