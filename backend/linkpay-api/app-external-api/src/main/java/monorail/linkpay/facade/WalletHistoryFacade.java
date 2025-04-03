package monorail.linkpay.facade;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.facade.dto.WalletHistoryListResponse;
import monorail.linkpay.facade.dto.WalletHistoryResponse;
import monorail.linkpay.history.dto.WalletHistoryDto;
import monorail.linkpay.history.dto.WalletHistoryListDto;
import monorail.linkpay.history.service.WalletHistoryService;
import monorail.linkpay.payment.dto.PaymentDto;
import monorail.linkpay.payment.service.PaymentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WalletHistoryFacade {

    private final WalletHistoryService walletHistoryService;
    private final PaymentService paymentService;

    public WalletHistoryResponse readWalletHistory(final Long walletHistoryId, final Long memberId) {
        WalletHistoryDto walletHistoryDto = walletHistoryService.readWalletHistory(walletHistoryId, memberId);
        List<PaymentDto> paymentDtos =  paymentService.readPaymentsByHistoryIdIn(Set.of(walletHistoryDto.walletHistoryId()));
        return WalletHistoryResponse.from(walletHistoryDto, paymentDtos.stream().findAny().orElse(null));
    }

    public WalletHistoryListResponse readLinkedWalletHistoryPage(final Long walletId,
                                                                 final Long lastId,
                                                                 final int size) {
        WalletHistoryListDto walletHistoryListDto = walletHistoryService.readLinkedWalletHistoryPage(walletId, lastId, size);
        return toWalletHistoryListResponse(walletHistoryListDto);
    }

    public WalletHistoryListResponse readMyWalletHistoryPage(final Long memberId,
                                                        final Long lastId,
                                                        final int size) {
        WalletHistoryListDto walletHistoryListDto = walletHistoryService.readMyWalletHistoryPage(memberId, lastId, size);
        return toWalletHistoryListResponse(walletHistoryListDto);
    }

    private WalletHistoryListResponse toWalletHistoryListResponse(WalletHistoryListDto walletHistoryListDto) {
        var walletHistoryIds = extractWalletHistoryIds(walletHistoryListDto);
        var paymentDtos = paymentService.readPaymentsByHistoryIdIn(walletHistoryIds);
        return WalletHistoryListResponse.from(walletHistoryListDto, paymentDtos);
    }

    private Set<Long> extractWalletHistoryIds(WalletHistoryListDto dto) {
        return dto.walletHistoryDtos().stream()
                .map(WalletHistoryDto::walletHistoryId)
                .collect(Collectors.toUnmodifiableSet());
    }
}
