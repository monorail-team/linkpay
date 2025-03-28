package monorail.linkpay.history.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.history.domain.WalletHistory;
import monorail.linkpay.history.dto.WalletHistoryListResponse;
import monorail.linkpay.history.dto.WalletHistoryResponse;
import monorail.linkpay.wallet.domain.Wallet;
import monorail.linkpay.wallet.service.MyWalletFetcher;
import monorail.linkpay.wallet.service.WalletFetcher;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class WalletHistoryService {

    private final WalletHistoryFetcher walletHistoryFetcher;
    private final MyWalletFetcher myWalletFetcher;
    private final WalletFetcher walletFetcher;

    public WalletHistoryResponse readWalletHistory(final Long id) {
        WalletHistory walletHistory = walletHistoryFetcher.fetchById(id);
        return WalletHistoryResponse.from(walletHistory);
    }

    public WalletHistoryListResponse readMyWalletHistoryPage(final Long memberId,
                                                             final Long lastId,
                                                             final int size) {
        Wallet wallet = myWalletFetcher.fetchByMemberId(memberId);
        Slice<WalletHistory> walletHistories = walletHistoryFetcher.fetchPageByWalletId(wallet.getId(), lastId, size);
        return WalletHistoryListResponse.from(walletHistories);
    }

    public WalletHistoryListResponse readLinkedWalletHistoryPage(final Long walletId,
                                                                 final Long lastId,
                                                                 final int size) {
        Slice<WalletHistory> walletHistories = walletHistoryFetcher.fetchPageByWalletId(walletId, lastId, size);
        return WalletHistoryListResponse.from(walletHistories);
    }
}
