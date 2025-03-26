package monorail.linkpay.linkcard.service;

import static monorail.linkpay.linkcard.domain.CardState.UNREGISTERED;
import static monorail.linkpay.linkcard.domain.CardType.OWNED;
import static monorail.linkpay.linkcard.domain.CardType.SHARED;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.linkcard.domain.CardColor;
import monorail.linkpay.linkcard.domain.CardState;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.linkcard.dto.LinkCardResponse;
import monorail.linkpay.linkcard.dto.LinkCardsResponse;
import monorail.linkpay.linkcard.repository.LinkCardRepository;
import monorail.linkpay.linkcard.service.request.LinkCardCreateServiceRequest;
import monorail.linkpay.linkcard.service.request.SharedLinkCardCreateServiceRequest;
import monorail.linkpay.linkedwallet.domain.LinkedWallet;
import monorail.linkpay.linkedwallet.service.LinkedWalletFetcher;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.member.service.MemberFetcher;
import monorail.linkpay.util.id.IdGenerator;
import monorail.linkpay.wallet.domain.Wallet;
import monorail.linkpay.wallet.service.WalletFetcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
    private final LinkedWalletFetcher linkedWalletFetcher;

    @Transactional
    public void create(Long creatorId, LinkCardCreateServiceRequest request) {
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

    @Transactional
    public void createShared(SharedLinkCardCreateServiceRequest request) {
        LinkedWallet linkedwallet = linkedWalletFetcher.fetchById(request.linkedWalletId());

        for (long memberId : request.memberIds()) {
            Member member = memberFetcher.fetchById(memberId);

            linkCardRepository.save(LinkCard.builder()
                    .id(idGenerator.generate())
                    .cardColor(CardColor.getRandomColor())
                    .cardName(request.cardName())
                    .cardType(SHARED)
                    .linkedWallet(linkedwallet)
                    .limitPrice(request.limitPrice())
                    .member(member)
                    .expiredAt(request.expiratedAt().plusDays(1).atStartOfDay())
                    .usedPoint(new Point(0))
                    .state(UNREGISTERED)
                    .build());
        }
    }

    public LinkCardsResponse read(final Long memberId, final Long lastId, final int size) {
        Pageable pageable = PageRequest.of(0, size);
        Slice<LinkCard> linkCards = linkCardRepository.findWithLastId(memberId, lastId, pageable);
        return new LinkCardsResponse(
                getLinkCardResponses(linkCards),
                linkCards.hasNext()
        );
    }

    public LinkCardsResponse readByState(final long memberId, final Long lastId, final int size,
                                         final CardState state, final LocalDateTime now) {
        Pageable pageable = PageRequest.of(0, size);
        Slice<LinkCard> linkCards = linkCardRepository.findByStateWithLastId(memberId, lastId, pageable, state, now);
        return new LinkCardsResponse(
                getLinkCardResponses(linkCards),
                linkCards.hasNext()
        );
    }

    private static List<LinkCardResponse> getLinkCardResponses(Slice<LinkCard> linkCards) {
        return linkCards.stream()
                .map(LinkCardResponse::from)
                .toList();
    }

    @Transactional
    public void registLinkCard(final List<Long> linkCardIds) {
        linkCardRepository.updateStateById(new HashSet<>(linkCardIds));
    }


}
