package monorail.linkpay.wallet.service;

import static monorail.linkpay.common.domain.TransactionType.DEPOSIT;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.history.service.WalletHistoryRecorder;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.member.service.MemberFetcher;
import monorail.linkpay.util.id.IdGenerator;
import monorail.linkpay.wallet.domain.MyWallet;
import monorail.linkpay.wallet.domain.Wallet;
import monorail.linkpay.wallet.dto.WalletResponse;
import monorail.linkpay.wallet.repository.MyWalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyWalletService {

    private final MyWalletRepository myWalletRepository;
    private final WalletFetcher walletFetcher;
    private final IdGenerator idGenerator;
    private final WalletHistoryRecorder walletHistoryRecorder;
    private final MemberFetcher memberFetcher;

    @Transactional
    public Long create(final Member member) {
        return myWalletRepository.save(MyWallet.builder()
                .id(idGenerator.generate())
                .member(member)
                .build()).getId();
    }

    public WalletResponse read(final Long memberId) {
        Wallet wallet = walletFetcher.fetchByMemberId(memberId);
        return new WalletResponse(wallet.readAmount());
    }

    @Transactional
    public void charge(final Long memberId, final Point point) {
        Member member = memberFetcher.fetchById(memberId);
        Wallet wallet = walletFetcher.fetchByMemberIdForUpdate(memberId);
        wallet.chargePoint(point);
        walletHistoryRecorder.recordHistory(DEPOSIT, wallet, point, member);
    }

    @Transactional
    public void deduct(final Long memberId, final Point point) {
        Member member = memberFetcher.fetchById(memberId);
        Wallet wallet = walletFetcher.fetchByMemberIdForUpdate(memberId);
        wallet.deductPoint(point);
        walletHistoryRecorder.recordHistory(DEPOSIT, wallet, point, member);
    }
}
