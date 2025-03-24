package monorail.linkpay.linkcard.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.linkcard.domain.CardColor;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.linkcard.dto.LinkCardResponse;
import monorail.linkpay.linkcard.dto.LinkCardsResponse;
import monorail.linkpay.linkcard.repository.LinkCardRepository;
import monorail.linkpay.linkcard.service.request.CreateLinkCardServiceRequest;
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

import java.time.LocalDate;
import java.util.List;

import static monorail.linkpay.exception.ExceptionCode.INVALID_REQUEST;
import static monorail.linkpay.linkcard.domain.CardState.UNREGISTERED;
import static monorail.linkpay.linkcard.domain.CardType.OWNED;

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
        if(request.expiratedAt().isBefore(LocalDate.now())){
            throw new LinkPayException(INVALID_REQUEST, "만료일은 현재일 이전으로 설정할 수 없습니다.");
        }

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

    public LinkCardsResponse read(final Long memberId, final Long lastId, final int size) {
        Pageable pageable = PageRequest.of(0, size);
        Wallet wallet = walletFetcher.fetchByMemberId(memberId);
        Slice<LinkCard> linkCards = linkCardRepository.findByWalletWithLastId(wallet, lastId, pageable);
        // todo: 링크지갑 만들면 해당 지갑 연결된 카드도 들고오기
        return new LinkCardsResponse(
                getLinkCardResponses(linkCards),
                linkCards.hasNext()
        );

    }

    public LinkCardsResponse readUnregister(final long memberId, final Long lastId, final int size) {
        Pageable pageable = PageRequest.of(0, size);
        Wallet wallet = walletFetcher.fetchByMemberId(memberId);
        // todo: 링크지갑 만들면 해당 지갑 연결된 카드도 들고오기
        Slice<LinkCard> linkCards = linkCardRepository.findUnregisterByWalletWithLastId(wallet, lastId, pageable);
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
}
