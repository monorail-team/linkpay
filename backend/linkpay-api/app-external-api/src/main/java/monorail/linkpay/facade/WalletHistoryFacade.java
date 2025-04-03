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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WalletHistoryFacade {

    private final WalletHistoryService walletHistoryService;
    private final PaymentService paymentService;

    public WalletHistoryResponse readWalletHistory(final Long walletHistoryId, final Long memberId) {
        WalletHistoryDto walletHistoryDto = walletHistoryService.readWalletHistory(walletHistoryId, memberId);
        PaymentDto paymentDto = paymentService.readPayment(walletHistoryDto.walletHistoryId());
        return WalletHistoryResponse.from(walletHistoryDto, paymentDto);
    }

    public WalletHistoryListResponse readLinkedWalletHistoryPage(final Long walletId,
                                                                 final Long lastId,
                                                                 final int size) {
        WalletHistoryListDto walletHistoryListDto = walletHistoryService.readLinkedWalletHistoryPage(walletId, lastId, size);
        List<WalletHistoryDto> walletHistoryDtos = walletHistoryListDto.walletHistoryDtos();
        List<PaymentDto> paymentDtos = paymentService.readPaymentsByHistoryIdIn(walletHistoryDtos.stream()
                .map(WalletHistoryDto::walletHistoryId)
                .collect(Collectors.toUnmodifiableSet()));
        return WalletHistoryListResponse.from(walletHistoryListDto, paymentDtos);
    }

    public WalletHistoryListResponse readMyWalletHistoryPage(final Long memberId,
                                                        final Long lastId,
                                                        final int size) {
        WalletHistoryListDto walletHistoryListDto = walletHistoryService.readMyWalletHistoryPage(memberId, lastId, size);
        List<WalletHistoryDto> walletHistoryDtos = walletHistoryListDto.walletHistoryDtos();
        List<PaymentDto> paymentDtos = paymentService.readPaymentsByHistoryIdIn(walletHistoryDtos.stream()
                .map(WalletHistoryDto::walletHistoryId)
                .collect(Collectors.toUnmodifiableSet()));
        return WalletHistoryListResponse.from(walletHistoryListDto, paymentDtos);
    }
}
