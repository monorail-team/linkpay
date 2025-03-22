package monorail.linkpay.linkcard.service;

import static monorail.linkpay.linkcard.domain.CardState.UNREGISTERED;
import static monorail.linkpay.linkcard.domain.CardType.OWNED;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.linkcard.domain.CardColor;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.linkcard.repository.LinkCardRepository;
import monorail.linkpay.linkcard.service.request.CreateLinkCardServiceRequest;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.member.service.MemberFetcher;
import monorail.linkpay.util.id.IdGenerator;
import monorail.linkpay.wallet.domain.Wallet;
import monorail.linkpay.wallet.service.WalletFetcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LinkCardService {

    private final LinkCardRepository linkCardRepository;
    private final WalletFetcher walletFetcher;
    private final MemberFetcher memberFetcher;
    private final IdGenerator idGenerator;

    @Transactional
    public void create(Long creatorId, CreateLinkCardServiceRequest request) {
        Wallet wallet = walletFetcher.fetchByMemberId(creatorId);

        Member member = memberFetcher.fetchById(creatorId);
        linkCardRepository.save(LinkCard.builder()
                .id(idGenerator.generate())
                .cardColor(CardColor.getRandomColor())
                .cardName(request.cardName())
                .cardType(OWNED)
                .wallet(wallet)
                .limitPrice(request.limitPrice())
                .member(member)
                .expiredAt(request.expiratedAt().plusDays(1).atStartOfDay())
                .usedPoint(new Point(0))
                .state(UNREGISTERED)
                .build());
    }
}
