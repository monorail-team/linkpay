package monorail.linkpay.wallet.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.wallet.domain.WalletHistory;
import monorail.linkpay.wallet.repository.WalletHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static monorail.linkpay.exception.ExceptionCode.NOT_FOUND_RESOURCE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WalletHistoryService {

    private final WalletHistoryRepository walletHistoryRepository;

    public WalletHistoryResponse read(final Long walletHistoryId) {
        WalletHistory walletHistory = walletHistoryRepository.findById(walletHistoryId)
            .orElseThrow(() -> new LinkPayException(NOT_FOUND_RESOURCE, "내역 아이디에 해당하는 내역이 존재하지 않습니다."));

        return new WalletHistoryResponse(walletHistory.getId(),
                walletHistory.getPoint().getAmount(),
                walletHistory.getTransactionType().toString(),
                walletHistory.getCreatedAt());
    }
}
