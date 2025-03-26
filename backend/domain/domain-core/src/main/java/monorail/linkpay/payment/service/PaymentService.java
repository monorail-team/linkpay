package monorail.linkpay.payment.service;

import static monorail.linkpay.common.domain.TransactionType.WITHDRAWAL;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.history.service.WalletHistoryRecorder;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.linkcard.service.LinkCardFetcher;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.member.service.MemberFetcher;
import monorail.linkpay.payment.domain.Payment;
import monorail.linkpay.payment.repository.PaymentRepository;
import monorail.linkpay.store.domain.Store;
import monorail.linkpay.store.service.StoreFetcher;
import monorail.linkpay.util.id.IdGenerator;
import monorail.linkpay.wallet.domain.Wallet;
import monorail.linkpay.wallet.service.WalletFetcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final WalletHistoryRecorder walletHistoryRecorder;
    private final WalletFetcher walletFetcher;
    private final MemberFetcher memberFetcher;
    private final LinkCardFetcher linkCardFetcher;
    private final StoreFetcher storeFetcher;
    private final IdGenerator idGenerator;

    public void pay(final Long memberId, final Point point, final Long linkCardId, final Long storeId) {
        Member member = memberFetcher.fetchById(memberId);
        LinkCard linkCard = linkCardFetcher.fetchByOwnerId(linkCardId, member);
        linkCard.validateExpiredDate();
        Store store = storeFetcher.fetchById(storeId);
        linkCard.usePoint(point);
        Wallet wallet = walletFetcher.fetchByMemberIdForUpdate(linkCard.getWallet().getId());
        wallet.deductPoint(point);

        walletHistoryRecorder.recordHistory(WITHDRAWAL, wallet, point, linkCard.getMember());
        paymentRepository.save(Payment.builder()
                .id(idGenerator.generate())
                .linkCard(linkCard)
                .member(linkCard.getMember())
                .amount(point)
                .store(store)
                .build());
    }
}
