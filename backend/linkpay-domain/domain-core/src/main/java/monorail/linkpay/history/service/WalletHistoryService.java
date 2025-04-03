package monorail.linkpay.history.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.history.dto.WalletHistoryDto;
import monorail.linkpay.history.dto.WalletHistoryListDto;
import monorail.linkpay.member.service.MemberFetcher;
import monorail.linkpay.wallet.service.MyWalletFetcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WalletHistoryService {

    private final WalletHistoryFetcher walletHistoryFetcher;
    private final MyWalletFetcher myWalletFetcher;
    private final MemberFetcher memberFetcher;
    private final WalletHistoryValidator validator;

    public WalletHistoryDto readWalletHistory(final Long walletHistoryId, final Long memberId) {
        var walletHistory = walletHistoryFetcher.fetchById(walletHistoryId);
        var member = memberFetcher.fetchById(memberId);
        validator.validateRead(walletHistory, member);
        return WalletHistoryDto.from(walletHistory);
    }

    public WalletHistoryListDto readMyWalletHistoryPage(final Long memberId,
                                                             final Long lastId,
                                                             final int size) {
        var myWallet = myWalletFetcher.fetchByMemberId(memberId);
        var walletHistories = walletHistoryFetcher.fetchPage(myWallet.getId(), lastId, size);
        return WalletHistoryListDto.from(walletHistories);
    }

    public WalletHistoryListDto readLinkedWalletHistoryPage(final Long walletId,
                                                            final Long lastId,
                                                            final int size) {
        var walletHistories = walletHistoryFetcher.fetchPage(walletId, lastId, size);
        return WalletHistoryListDto.from(walletHistories);
    }
}
