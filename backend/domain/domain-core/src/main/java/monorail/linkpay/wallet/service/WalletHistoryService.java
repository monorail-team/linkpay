package monorail.linkpay.wallet.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.wallet.domain.WalletHistory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WalletHistoryService {

    private final WalletHistoryFetcher walletHistoryFetcher;

    public WalletHistoryResponse read(final Long id) {
        WalletHistory walletHistory = walletHistoryFetcher.fetchById(id);
        return new WalletHistoryResponse(walletHistory.getId(),
                walletHistory.getPoint().getAmount(),
                walletHistory.getTransactionType().toString(),
                walletHistory.getCreatedAt());
    }
}
