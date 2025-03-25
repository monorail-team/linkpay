package monorail.linkpay.wallet.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.util.id.IdGenerator;
import monorail.linkpay.wallet.domain.Wallet;
import monorail.linkpay.wallet.dto.WalletResponse;
import monorail.linkpay.wallet.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static monorail.linkpay.common.domain.TransactionType.DEPOSIT;
import static monorail.linkpay.common.domain.TransactionType.WITHDRAWAL;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletFetcher walletFetcher;
    private final IdGenerator idGenerator;

    @Transactional
    public Long create(final Member member) {
        return walletRepository.save(Wallet.builder()
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
        Wallet wallet = walletFetcher.fetchByMemberIdForUpdate(memberId);
        wallet.chargePoint(point);
    }

    @Transactional
    public void deduct(final Long memberId, final Point point) {
        Wallet wallet = walletFetcher.fetchByMemberIdForUpdate(memberId);
        wallet.deductPoint(point);
    }
}
