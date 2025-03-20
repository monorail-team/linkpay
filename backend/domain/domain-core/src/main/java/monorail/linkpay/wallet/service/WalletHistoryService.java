package monorail.linkpay.wallet.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.wallet.domain.WalletHistory;
import monorail.linkpay.wallet.repository.WalletHistoryRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WalletHistoryService {

    private final WalletHistoryFetcher walletHistoryFetcher;
    private final WalletHistoryRepository walletHistoryRepository;

    public WalletHistoryResponse read(final Long id) {
        WalletHistory walletHistory = walletHistoryFetcher.fetchById(id);
        return new WalletHistoryResponse(walletHistory.getId(),
                walletHistory.getPoint().getAmount(),
                walletHistory.getTransactionType().toString(),
                walletHistory.getCreatedAt());
    }

    public WalletHistoryListResponse readPage(final Long walletId, final Long lastId, final int size) {
        Pageable pageable = PageRequest.of(0, size);
        Slice<WalletHistory> walletHistories = walletHistoryRepository.findByWalletIdWithLastId(walletId, lastId, pageable);
        List<WalletHistoryResponse> walletHistoryResponses = new ArrayList<>();

        for (WalletHistory walletHistory : walletHistories) {
            walletHistoryResponses.add(new WalletHistoryResponse(
                    walletHistory.getId(),
                    walletHistory.getPoint().getAmount(),
                    walletHistory.getTransactionType().toString(),
                    walletHistory.getCreatedAt()));
        }

        return new WalletHistoryListResponse(
            walletHistoryResponses,
            walletHistories.hasNext());
    }
}
