package monorail.linkpay.wallet.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.util.id.IdGenerator;
import monorail.linkpay.wallet.domain.MyWallet;
import monorail.linkpay.wallet.dto.WalletResponse;
import monorail.linkpay.wallet.repository.MyWalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyWalletService {

    private final MyWalletRepository myWalletRepository;
    private final MyWalletFetcher myWalletFetcher;
    private final WalletUpdater walletUpdater;
    private final IdGenerator idGenerator;

    @Transactional
    public Long create(final Member member) {
        return myWalletRepository.save(MyWallet.builder()
                .id(idGenerator.generate())
                .member(member)
                .build()).getId();
    }

    public WalletResponse read(final Long memberId) {
        MyWallet wallet = myWalletFetcher.fetchByMemberId(memberId);
        return new WalletResponse(wallet.readAmount());
    }

    @Transactional
    public void charge(final Long memberId, final Point point) {
        MyWallet wallet = myWalletFetcher.fetchByMemberIdForUpdate(memberId);
        walletUpdater.chargePoint(wallet, point, wallet.getMember());
    }
}
