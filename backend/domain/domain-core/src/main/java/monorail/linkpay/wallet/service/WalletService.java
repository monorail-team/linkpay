package monorail.linkpay.wallet.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.common.domain.TransactionType;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.util.id.IdGenerator;
import monorail.linkpay.wallet.domain.Wallet;
import monorail.linkpay.wallet.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletHistoryRecorder walletHistoryRecorder;
    private final WalletFetcher walletFetcher;
    private final IdGenerator idGenerator;

    @Transactional
    public Long create(final Member member) {
        return walletRepository.save(Wallet.builder()
            .id(idGenerator.generate())
            .point(new Point(0))
            .member(member)
            .build()).getId();
    }

    public WalletResponse read(final Long memberId) {
        Wallet wallet = walletFetcher.fetchByMemberId(memberId);
        return new WalletResponse(wallet.getAmount());
    }

    @Transactional
    public void charge(final Long memberId, final Long amount) {
        Wallet wallet = walletFetcher.fetchByMemberId(memberId);
        walletHistoryRecorder.record(wallet.getId(), amount, TransactionType.DEPOSIT);
        walletRepository.increaseWalletAmount(memberId, amount);
    }
}
