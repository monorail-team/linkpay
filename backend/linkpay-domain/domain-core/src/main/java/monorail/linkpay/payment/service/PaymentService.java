package monorail.linkpay.payment.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.linkcard.service.LinkCardFetcher;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.member.service.MemberFetcher;
import monorail.linkpay.payment.domain.Payment;
import monorail.linkpay.payment.repository.PaymentRepository;
import monorail.linkpay.store.service.StoreFetcher;
import monorail.linkpay.util.id.IdGenerator;
import monorail.linkpay.wallet.domain.Wallet;
import monorail.linkpay.wallet.service.WalletFetcher;
import monorail.linkpay.wallet.service.WalletUpdater;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final LinkCardFetcher linkCardFetcher;
    private final StoreFetcher storeFetcher;
    private final MemberFetcher memberFetcher;
    private final WalletUpdater walletUpdater;
    private final WalletFetcher walletFetcher;
    private final IdGenerator idGenerator;

    @Transactional
    public void createPayment(final Long memberId, final Point point, final Long linkCardId, final Long storeId) {
        Member member = memberFetcher.fetchById(memberId);
        LinkCard linkCard = linkCardFetcher.fetchByIdForUpdate(linkCardId);
        validateLinkCard(point, linkCard, member);
        linkCard.usePoint(point);

        Wallet wallet = walletFetcher.fetchByIdForUpdate(linkCard.getWallet().getId());
        Payment payment = paymentRepository.save(getPayment(linkCard, point, storeId));
        walletUpdater.deductPoint(wallet, point, member, payment);
    }

    private void validateLinkCard(final Point point, final LinkCard linkCard, final Member member) {
        linkCard.validateOwnership(member);
        linkCard.validateExpiredDate();
        linkCard.validateLimitPriceNotExceed(point);
    }

    private Payment getPayment(final LinkCard linkCard, final Point point, final Long storeId) {
        return Payment.builder()
                .id(idGenerator.generate())
                .linkCard(linkCard)
                .member(linkCard.getMember())
                .amount(point)
                .store(storeFetcher.fetchById(storeId))
                .build();
    }
}
