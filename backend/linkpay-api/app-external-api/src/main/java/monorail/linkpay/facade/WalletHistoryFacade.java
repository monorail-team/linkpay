package monorail.linkpay.facade;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.facade.dto.WalletHistoryListResponse;
import monorail.linkpay.facade.dto.WalletHistoryResponse;
import monorail.linkpay.history.dto.WalletHistoryDto;
import monorail.linkpay.history.dto.WalletHistoryListDto;
import monorail.linkpay.history.service.WalletHistoryService;
import monorail.linkpay.payment.service.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WalletHistoryFacade {

    private final WalletHistoryService walletHistoryService;
    private final PaymentService paymentService;

    public WalletHistoryResponse readWalletHistory(final Long walletHistoryId, final Long memberId) {
        var walletHistoryDto = walletHistoryService.readWalletHistory(walletHistoryId, memberId);
        var paymentDtos =  paymentService.readPaymentsByHistoryIdIn(Set.of(walletHistoryDto.walletHistoryId()));
        return WalletHistoryResponse.from(walletHistoryDto, paymentDtos.stream().findAny().orElse(null));
    }

    public WalletHistoryListResponse readLinkedWalletHistoryPage(final Long walletId,
                                                                 final Long lastId,
                                                                 final int size) {
        var walletHistoryListDto = walletHistoryService.readLinkedWalletHistoryPage(walletId, lastId, size);
        return toWalletHistoryListResponse(walletHistoryListDto);
    }

    public WalletHistoryListResponse readMyWalletHistoryPage(final Long memberId,
                                                             final Long lastId,
                                                             final int size) {
        var walletHistoryListDto = walletHistoryService.readMyWalletHistoryPage(memberId, lastId, size);
        return toWalletHistoryListResponse(walletHistoryListDto);
    }

    private WalletHistoryListResponse toWalletHistoryListResponse(final WalletHistoryListDto walletHistoryListDto) {
        var walletHistoryIds = extractWalletHistoryIds(walletHistoryListDto);
        var paymentDtos = paymentService.readPaymentsByHistoryIdIn(walletHistoryIds);
        return WalletHistoryListResponse.from(walletHistoryListDto, paymentDtos);
    }

    private Set<Long> extractWalletHistoryIds(final WalletHistoryListDto dto) {
        return dto.walletHistoryDtos().stream()
                .map(WalletHistoryDto::walletHistoryId)
                .collect(Collectors.toUnmodifiableSet());
    }
}