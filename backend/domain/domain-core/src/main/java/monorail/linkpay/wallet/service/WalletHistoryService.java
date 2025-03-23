package monorail.linkpay.wallet.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.wallet.domain.WalletHistory;
import monorail.linkpay.wallet.dto.WalletHistoryListResponse;
import monorail.linkpay.wallet.dto.WalletHistoryResponse;
import monorail.linkpay.wallet.repository.WalletHistoryRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WalletHistoryService {

    private final WalletHistoryFetcher walletHistoryFetcher;
    private final WalletHistoryRepository walletHistoryRepository;

    public WalletHistoryResponse read(final Long id) {
        WalletHistory walletHistory = walletHistoryFetcher.fetchById(id);
        return WalletHistoryResponse.from(walletHistory);
    }

    public WalletHistoryListResponse readPage(final Long walletId, final Long lastId, final int size) {
        Slice<WalletHistory> walletHistories = walletHistoryRepository
                .findByWalletIdWithLastId(walletId, lastId, PageRequest.of(0, size));

        List<WalletHistoryResponse> walletHistoryResponses = walletHistories.stream()
                .map(WalletHistoryResponse::from)
                .toList();

        return new WalletHistoryListResponse(
                walletHistoryResponses,
                walletHistories.hasNext());
    }
}
