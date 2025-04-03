package monorail.linkpay.facade.dto;

import monorail.linkpay.history.dto.WalletHistoryDto;
import monorail.linkpay.history.dto.WalletHistoryListDto;
import monorail.linkpay.payment.dto.PaymentDto;

import java.util.ArrayList;
import java.util.List;

public record WalletHistoryListResponse(
        List<WalletHistoryResponse> walletHistories,
        boolean hasNext
) {
    public static WalletHistoryListResponse from(final WalletHistoryListDto walletHistoryListDto, final List<PaymentDto> paymentDtos) {

        List<WalletHistoryResponse> walletHistories = new ArrayList<>();
        for (WalletHistoryDto wh : walletHistoryListDto.walletHistoryDtos()) {
            PaymentDto paymentDto = paymentDtos.stream().filter(p -> wh.walletHistoryId().equals(p.walletHistoryId())).findAny().get();
            walletHistories.add(WalletHistoryResponse.from(wh, paymentDto));
        }
        return new WalletHistoryListResponse(
                walletHistories,
                walletHistoryListDto.hasNext());
    }
}
