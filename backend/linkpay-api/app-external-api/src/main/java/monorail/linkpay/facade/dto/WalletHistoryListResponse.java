package monorail.linkpay.facade.dto;

import monorail.linkpay.history.dto.WalletHistoryDto;
import monorail.linkpay.history.dto.WalletHistoryListDto;
import monorail.linkpay.payment.dto.PaymentDto;

import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public record WalletHistoryListResponse(
        List<WalletHistoryResponse> walletHistories,
        boolean hasNext
) {

    public static WalletHistoryListResponse from(final WalletHistoryListDto walletHistoryListDto, final List<PaymentDto> paymentDtos) {
        var paymentMap = paymentDtos.stream()
                .collect(toMap(PaymentDto::walletHistoryId, identity()));

        var walletHistories = walletHistoryListDto.walletHistoryDtos().stream()
                .map(wh -> toWalletHistoryResponse(wh, paymentMap)).toList();

        return new WalletHistoryListResponse(walletHistories, walletHistoryListDto.hasNext());
    }

    private static WalletHistoryResponse toWalletHistoryResponse(WalletHistoryDto walletHistoryDto, Map<Long, PaymentDto> paymentMap) {
        return WalletHistoryResponse.from(walletHistoryDto, paymentMap.get(walletHistoryDto.walletHistoryId()));
    }


}
