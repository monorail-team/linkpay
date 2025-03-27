package monorail.linkpay.linkcard.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.history.service.WalletHistoryRecorder;
import monorail.linkpay.linkcard.domain.CardState;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.linkcard.dto.LinkCardResponse;
import monorail.linkpay.linkcard.dto.LinkCardsResponse;
import monorail.linkpay.linkcard.repository.LinkCardRepository;
import monorail.linkpay.linkcard.service.request.LinkCardCreateServiceRequest;
import monorail.linkpay.linkcard.service.request.SharedLinkCardCreateServiceRequest;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.member.repository.MemberRepository;
import monorail.linkpay.member.service.MemberFetcher;
import monorail.linkpay.store.service.StoreFetcher;
import monorail.linkpay.util.id.IdGenerator;
import monorail.linkpay.wallet.domain.LinkedWallet;
import monorail.linkpay.wallet.domain.Wallet;
import monorail.linkpay.wallet.service.LinkedMemberFetcher;
import monorail.linkpay.wallet.service.LinkedWalletFetcher;
import monorail.linkpay.wallet.service.MyWalletFetcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LinkCardService {

    private final LinkCardRepository linkCardRepository;
    private final MemberRepository memberRepository;
    private final LinkedWalletFetcher linkedWalletFetcher;
    private final MyWalletFetcher myWalletFetcher;
    private final MemberFetcher memberFetcher;
    private final IdGenerator idGenerator;

    @Transactional
    public void create(final Long memberId, final LinkCardCreateServiceRequest request) {
        Wallet wallet = myWalletFetcher.fetchByMemberId(memberId);
        Member member = memberFetcher.fetchById(memberId);
        linkCardRepository.save(request.toLinkCard(idGenerator.generate(), wallet, member));
    }

    @Transactional
    public void createShared(final SharedLinkCardCreateServiceRequest request) {
        LinkedWallet linkedwallet = linkedWalletFetcher.fetchById(request.linkedWalletId());
        List<LinkCard> linkCards = memberRepository.findMembersByIdIn(new HashSet<>(request.memberIds())).stream()
                .map(member -> request.toLinkCard(idGenerator.generate(), member, linkedwallet))
                .toList();

        linkCardRepository.saveAll(linkCards);
    }

    public LinkCardsResponse read(final Long memberId, final Long lastId, final int size) {
        Slice<LinkCard> linkCards = linkCardRepository.findWithLastId(memberId, lastId, PageRequest.of(0, size));
        return new LinkCardsResponse(getLinkCardResponses(linkCards), linkCards.hasNext());
    }

    public LinkCardsResponse readByState(final long memberId, final Long lastId, final int size,
                                         final CardState state, final LocalDateTime now) {
        Slice<LinkCard> linkCards = linkCardRepository
                .findByStateWithLastId(memberId, lastId, state, now, PageRequest.of(0, size));
        return new LinkCardsResponse(getLinkCardResponses(linkCards), linkCards.hasNext());
    }

    private List<LinkCardResponse> getLinkCardResponses(final Slice<LinkCard> linkCards) {
        return linkCards.stream()
                .map(LinkCardResponse::from)
                .toList();
    }

    @Transactional
    public void activateLinkCard(final List<Long> linkCardIds) {
        linkCardRepository.updateStateByIds(new HashSet<>(linkCardIds));
    }
}
