package monorail.linkpay.wallet.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.member.service.MemberFetcher;
import monorail.linkpay.util.id.IdGenerator;
import monorail.linkpay.wallet.domain.Wallet;
import monorail.linkpay.wallet.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static monorail.linkpay.exception.ExceptionCode.NOT_FOUND_RESOURCE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WalletService {

    private final WalletRepository walletRepository;
    private final MemberFetcher memberFetcher;
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
        Member member = memberFetcher.fetchById(memberId);
        Wallet wallet = walletRepository.findByMember(member)
            .orElseThrow(() -> new LinkPayException(NOT_FOUND_RESOURCE, "멤버 아이디에 해당하는 지갑이 존재하지 않습니다."));
        return new WalletResponse(wallet.getAmount());
    }

    @Transactional
    public void charge(final Long memberId, final Long amount) {
        Member member = memberFetcher.fetchById(memberId);
        walletRepository.updateAmount(memberId, amount);
    }
}
