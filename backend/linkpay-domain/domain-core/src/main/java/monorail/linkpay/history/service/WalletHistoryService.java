package monorail.linkpay.history.service;

import static monorail.linkpay.exception.ExceptionCode.INVALID_REQUEST;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.history.domain.WalletHistory;
import monorail.linkpay.history.dto.WalletHistoryListResponse;
import monorail.linkpay.history.dto.WalletHistoryResponse;
import monorail.linkpay.history.repository.WalletHistoryRepository;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.member.service.MemberFetcher;
import monorail.linkpay.wallet.domain.Wallet;
import monorail.linkpay.wallet.repository.LinkedMemberRepository;
import monorail.linkpay.wallet.repository.LinkedWalletRepository;
import monorail.linkpay.wallet.repository.MyWalletRepository;
import monorail.linkpay.wallet.repository.WalletRepository;
import monorail.linkpay.wallet.service.MyWalletFetcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class WalletHistoryService {

    private final WalletHistoryFetcher walletHistoryFetcher;
    private final MyWalletFetcher myWalletFetcher;
    private final WalletHistoryRepository walletHistoryRepository;
    private final MemberFetcher memberFetcher;
    private final WalletRepository walletRepository;
    private final LinkedMemberRepository linkedMemberRepository;
    private final LinkedWalletRepository linkedWalletRepository;
    private final MyWalletRepository myWalletRepository;

    public WalletHistoryResponse readWalletHistory(final Long walletHistoryId, final Long memberId) {
        WalletHistory walletHistory = walletHistoryFetcher.fetchById(walletHistoryId);
        Member member = memberFetcher.fetchById(memberId);
        validateOwnershipOrParticipant(walletHistory.getWallet(), member);
        return WalletHistoryResponse.from(walletHistory);
    }

    public WalletHistoryListResponse readMyWalletHistoryPage(final Long memberId,
                                                             final Long lastId,
                                                             final int size) {
        Wallet wallet = myWalletFetcher.fetchByMemberId(memberId);
        Slice<WalletHistory> walletHistories = walletHistoryRepository
                .findByWalletIdWithLastId(wallet.getId(), lastId, PageRequest.of(0, size));
        return WalletHistoryListResponse.from(walletHistories);
    }

    public WalletHistoryListResponse readLinkedWalletHistoryPage(final Long walletId,
                                                                 final Long lastId,
                                                                 final int size) {
        Slice<WalletHistory> walletHistories = walletHistoryRepository
                .findByWalletIdWithLastId(walletId, lastId, PageRequest.of(0, size));
        return WalletHistoryListResponse.from(walletHistories);
    }

    private void validateOwnershipOrParticipant(final Wallet wallet, final Member member) {
        myWalletRepository.findById(wallet.getId()).ifPresentOrElse(myWallet -> {
                    if (!myWallet.isMyWallet(member)) {
                        throw new LinkPayException(INVALID_REQUEST, "소유한 지갑이 아닙니다.");
                    }
                }, () ->
                        linkedMemberRepository.findByLinkedWalletIdAndMemberId(wallet.getId(), member.getId())
                                .orElseThrow(() -> new LinkPayException(INVALID_REQUEST, "참여중인 링크지갑이 아닙니다."))
        );
    }
}
