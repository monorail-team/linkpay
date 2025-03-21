package monorail.linkpay.wallet.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.util.id.IdGenerator;
import monorail.linkpay.wallet.domain.Wallet;
import monorail.linkpay.wallet.dto.WalletResponse;
import monorail.linkpay.wallet.repository.WalletRepository;
import org.hibernate.dialect.lock.OptimisticEntityLockException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static monorail.linkpay.common.domain.TransactionType.DEPOSIT;
import static monorail.linkpay.common.domain.TransactionType.WITHDRAWAL;
import static monorail.linkpay.exception.ExceptionCode.INVALID_REQUEST;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletHistoryRecorder walletHistoryRecorder;
    private final WalletFetcher walletFetcher;
    private final IdGenerator idGenerator;
    private static final int MAX_RETRIES = 3;

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
    public void charge(final Long memberId, final Point point) {
        Wallet wallet = walletFetcher.fetchByMemberId(memberId);
        Point remaining = wallet.getPoint().add(point);
        walletRepository.increaseWalletAmount(memberId, point.getAmount());
        walletHistoryRecorder.record(wallet, point, remaining, DEPOSIT);
    }

    @Transactional
    public void deduct(final Long memberId, final Point point) {
        Wallet wallet = walletFetcher.fetchByMemberId(memberId);
        Point remaining = wallet.getPoint().subtract(point);
        walletRepository.decreaseWalletAmount(wallet.getId(), point.getAmount());
        walletHistoryRecorder.record(wallet, point, remaining, WITHDRAWAL);
    }
}
