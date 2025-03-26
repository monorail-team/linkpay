package monorail.linkpay.history.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.history.domain.WalletHistory;
import monorail.linkpay.history.dto.WalletHistoryListResponse;
import monorail.linkpay.history.dto.WalletHistoryResponse;
import monorail.linkpay.history.repository.WalletHistoryRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
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
