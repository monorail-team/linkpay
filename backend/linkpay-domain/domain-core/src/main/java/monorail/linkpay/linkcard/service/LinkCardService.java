package monorail.linkpay.linkcard.service;

import static monorail.linkpay.exception.ExceptionCode.INVALID_REQUEST;
import static monorail.linkpay.wallet.domain.Role.CREATOR;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.linkcard.domain.CardState;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.linkcard.dto.LinkCardDetailResponse;
import monorail.linkpay.linkcard.dto.LinkCardHistoriesResponse;
import monorail.linkpay.linkcard.dto.LinkCardHistoryResponse;
import monorail.linkpay.linkcard.dto.LinkCardResponse;
import monorail.linkpay.linkcard.dto.LinkCardsResponse;
import monorail.linkpay.linkcard.repository.LinkCardQueryFactory;
import monorail.linkpay.linkcard.repository.LinkCardQueryRepository;
import monorail.linkpay.linkcard.repository.LinkCardRepository;
import monorail.linkpay.linkcard.service.request.LinkCardCreateServiceRequest;
import monorail.linkpay.linkcard.service.request.SharedLinkCardCreateServiceRequest;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.member.repository.MemberRepository;
import monorail.linkpay.member.service.MemberFetcher;
import monorail.linkpay.payment.domain.Payment;
import monorail.linkpay.payment.repository.PaymentRepository;
import monorail.linkpay.util.id.IdGenerator;
import monorail.linkpay.wallet.domain.LinkedMember;
import monorail.linkpay.wallet.domain.LinkedWallet;
import monorail.linkpay.wallet.domain.Wallet;
import monorail.linkpay.wallet.repository.LinkedMemberRepository;
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
    private final LinkCardQueryRepository linkCardQueryRepository;
    private final LinkedMemberRepository linkedMemberRepository;
    private final MemberRepository memberRepository;
    private final LinkedMemberFetcher linkedMemberFetcher;
    private final LinkedWalletFetcher linkedWalletFetcher;
    private final MyWalletFetcher myWalletFetcher;
    private final MemberFetcher memberFetcher;
    private final IdGenerator idGenerator;
    private final LinkCardFetcher linkCardFetcher;
    private final PaymentRepository paymentRepository;

    @Transactional
    public void create(final Long memberId, final LinkCardCreateServiceRequest request) {
        Wallet wallet = myWalletFetcher.fetchByMemberId(memberId);
        Member member = memberFetcher.fetchById(memberId);
        linkCardRepository.save(request.toLinkCard(idGenerator.generate(), wallet, member));
    }

    @Transactional
    public void createShared(final SharedLinkCardCreateServiceRequest request, final Long memberId) {
        LinkedWallet linkedwallet = linkedWalletFetcher.fetchById(Long.parseLong(request.linkedWalletId()));
        LinkedMember linkedWalletCreator = linkedMemberRepository.findByLinkedWalletIdAndRole(
                linkedwallet.getId(), CREATOR);

        if (!linkedWalletCreator.getMember().getId().equals(memberId)) {
            throw new LinkPayException(INVALID_REQUEST, "링크카드 생성 권한이 없습니다.");
        }

        List<Long> memberIds = linkedMemberRepository.findByLinkedWalletId(linkedwallet.getId()).stream()
                .map(linkedMember -> linkedMember.getMember().getId())
                .toList();

        boolean hasUnregisteredMember = request.memberIds().stream()
                .anyMatch(id -> !memberIds.contains(Long.parseLong(id)));
        if (hasUnregisteredMember) {
            throw new LinkPayException(INVALID_REQUEST, "해당 링크지갑에 참여하지 않은 사용자입니다.");
        }
        List<LinkCard> linkCards = memberRepository.findMembersByIdIn(
                        new HashSet<>(request.memberIds().stream().map(Long::parseLong).toList())).stream()
                .map(member -> request.toLinkCard(idGenerator.generate(), member, linkedwallet))
                .toList();

        linkCardRepository.saveAll(linkCards);
    }

    public LinkCardsResponse read(final Long memberId, final Long lastId, final int size, final String type) {
        Slice<LinkCard> linkCards = linkCardQueryRepository.findLinkCardsByMemberId(
                LinkCardQueryFactory.from(type),
                memberId,
                lastId,
                PageRequest.of(0, size)
        );
        return new LinkCardsResponse(getLinkCardResponses(linkCards), linkCards.hasNext());
    }

    public LinkCardsResponse readByState(final long memberId, final Long lastId, final int size,
                                         final CardState state, final LocalDateTime now) {
        Slice<LinkCard> linkCards = linkCardRepository.findByStateWithLastId(
                memberId,
                lastId,
                state,
                now,
                PageRequest.of(0, size)
        );
        return new LinkCardsResponse(getLinkCardResponses(linkCards), linkCards.hasNext());
    }

    private List<LinkCardResponse> getLinkCardResponses(final Slice<LinkCard> linkCards) {
        return linkCards.stream()
                .map(LinkCardResponse::from)
                .toList();
    }

    @Transactional
    public void activateLinkCard(final List<Long> linkCardIds, final Long memberId) {
        List<Long> memberLinkCardIds = linkCardRepository.findByMemberId(memberId).stream()
                .map(LinkCard::getId)
                .toList();

        boolean containsUnownedCard = linkCardIds.stream()
                .anyMatch(id -> !memberLinkCardIds.contains(id));
        if (containsUnownedCard) {
            throw new LinkPayException(INVALID_REQUEST, "소유하지 않은 카드 아이디입니다.");
        }
        linkCardRepository.updateStateByIds(new HashSet<>(linkCardIds));
    }

    @Transactional
    public void deleteLinkCard(final Long linkCardId, final Long memberId) {
        LinkCard linkCard = linkCardFetcher.fetchById(linkCardId);
        Member member = memberFetcher.fetchById(memberId);
        validateOwnershipOrCreator(linkCard, member);
        linkCardRepository.deleteById(linkCardId);
    }

    public LinkCardDetailResponse getLinkCardDetails(final Long memberId, final Long linkCardId) {
        LinkCard linkCard = linkCardFetcher.fetchById(linkCardId);
        Member member = memberFetcher.fetchById(memberId);
        validateOwnershipOrCreator(linkCard, member);
        if (!linkCard.isSharedCard()) {
            return LinkCardDetailResponse.from(linkCard, null);
        }
        String linkedWalletName = linkedWalletFetcher.fetchById(linkCard.getWalletId()).getName();
        return LinkCardDetailResponse.from(linkCard, linkedWalletName);
    }

    public LinkCardHistoriesResponse getLinkCardHistories(final Long memberId, final Long lastId, final int size,
                                                          final Long linkCardId) {
        LinkCard linkCard = linkCardFetcher.fetchById(linkCardId);
        Member member = memberFetcher.fetchById(memberId);
        validateOwnershipOrCreator(linkCard, member);
        Slice<Payment> payments = paymentRepository.findPaymentsByLinkCard(
                linkCardId,
                lastId,
                PageRequest.of(0, size)
        );
        return new LinkCardHistoriesResponse(getLinkCardHistoryResponses(linkCard, payments), payments.hasNext());
    }

    private List<LinkCardHistoryResponse> getLinkCardHistoryResponses(final LinkCard linkCard,
                                                                      final Slice<Payment> payments
    ) {
        return payments.stream()
                .map(payment -> LinkCardHistoryResponse.from(linkCard, payment))
                .toList();
    }

    private void validateOwnershipOrCreator(final LinkCard linkCard, final Member member) {
        if (!linkCard.isSharedCard() || !isCreator(linkCard, member)) {
            linkCard.validateOwnership(member.getId());
        }
    }

    private boolean isCreator(final LinkCard linkCard, final Member member) {
        LinkedMember linkedMember = linkedMemberFetcher.fetchByLinkedWalletIdAndMemberId(
                linkCard.getWalletId(), member.getId());
        return linkedMember.isCreator();
    }
}
