package monorail.linkpay.linkcard;

import static java.time.LocalDateTime.now;
import static monorail.linkpay.linkcard.domain.CardState.REGISTERED;
import static monorail.linkpay.linkcard.domain.CardState.UNREGISTERED;
import static monorail.linkpay.linkcard.domain.CardType.OWNED;
import static monorail.linkpay.wallet.domain.Role.CREATOR;
import static monorail.linkpay.wallet.domain.Role.PARTICIPANT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import monorail.linkpay.common.IntegrationTest;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.linkcard.domain.CardColor;
import monorail.linkpay.linkcard.domain.CardState;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.linkcard.dto.LinkCardDetailResponse;
import monorail.linkpay.linkcard.dto.LinkCardHistoriesResponse;
import monorail.linkpay.linkcard.dto.LinkCardsResponse;
import monorail.linkpay.linkcard.service.LinkCardService;
import monorail.linkpay.linkcard.service.request.LinkCardCreateServiceRequest;
import monorail.linkpay.linkcard.service.request.SharedLinkCardCreateServiceRequest;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.payment.domain.Payment;
import monorail.linkpay.store.domain.Store;
import monorail.linkpay.wallet.domain.LinkedMember;
import monorail.linkpay.wallet.domain.LinkedWallet;
import monorail.linkpay.wallet.domain.MyWallet;
import monorail.linkpay.wallet.domain.Wallet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class LinkCardServiceTest extends IntegrationTest {

    @Autowired
    private LinkCardService linkCardService;

    @Test
    void 내_지갑에서_카드를_생성한다() {
        // given
        LinkCardCreateServiceRequest request = createCard(LocalDate.now().plusDays(1));

        // when
        linkCardService.create(member.getId(), request);

        List<LinkCard> result = linkCardRepository.findByMemberId(member.getId());
        assertThat(result).isNotNull();
    }

    @Test
    void 링크지갑에서_카드를_생성한다() {
        // given
        Member member1 = createMember("test@email.com", "u1");
        memberRepository.saveAll(List.of(member1));
        linkedWalletRepository.save(LinkedWallet.builder().id(1L).name("링크지갑").build());
        LinkedWallet linkedWallet = linkedWalletRepository.findById(1L).orElseThrow();
        linkedMemberRepository.saveAll(List.of(
                createLinkedMember(CREATOR, member, linkedWallet),
                createLinkedMember(PARTICIPANT, member1, linkedWallet)));
        SharedLinkCardCreateServiceRequest request = createSharedCards(LocalDate.now().plusDays(1),
                linkedWallet.getId().toString(),
                List.of(member.getId().toString(), member1.getId().toString()));

        // when
        linkCardService.createShared(request, member.getId());

        List<LinkCard> result = linkCardRepository.findByMemberId(member.getId());
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void 링크지갑_생성자가_아닌사람이_카드생성_시도시_오류가_발생한다() {
        // given
        Member member1 = createMember("test@email.com", "u1");
        memberRepository.saveAll(List.of(member1));
        linkedWalletRepository.save(LinkedWallet.builder().id(1L).name("링크지갑").build());
        LinkedWallet linkedWallet = linkedWalletRepository.findById(1L).orElseThrow();
        linkedMemberRepository.saveAll(List.of(
                createLinkedMember(CREATOR, member, linkedWallet),
                createLinkedMember(PARTICIPANT, member1, linkedWallet)));

        SharedLinkCardCreateServiceRequest request = createSharedCards(LocalDate.now().plusDays(1),
                linkedWallet.getId().toString(),
                List.of(member.getId().toString(), member1.getId().toString()));

        // when // then
        assertThatThrownBy(() -> linkCardService.createShared(request, member1.getId())).isInstanceOf(
                        LinkPayException.class)
                .hasMessage("링크카드 생성 권한이 없습니다.");
    }

    @Test
    void 링크지갑_참여자가_아닌사람에게_카드생성_시도시_오류가_발생한다() {
        // given
        Member member1 = createMember("test@email.com", "u1");
        Member member2 = createMember("test@email.com", "u1");
        memberRepository.saveAll(List.of(member1));
        linkedWalletRepository.save(LinkedWallet.builder().id(1L).name("링크지갑").build());
        LinkedWallet linkedWallet = linkedWalletRepository.findById(1L).orElseThrow();
        linkedMemberRepository.saveAll(List.of(
                createLinkedMember(CREATOR, member, linkedWallet),
                createLinkedMember(PARTICIPANT, member1, linkedWallet)));
        SharedLinkCardCreateServiceRequest request = createSharedCards(LocalDate.now().plusDays(1),
                linkedWallet.getId().toString(),
                List.of(member.getId().toString(), member2.getId().toString()));

        // when // then
        assertThatThrownBy(() -> linkCardService.createShared(request, member.getId())).isInstanceOf(
                        LinkPayException.class)
                .hasMessage("해당 링크지갑에 참여하지 않은 사용자입니다.");
    }

    @Test
    void 보유한_링크카드_중_내지갑카드를_조회한다() {
        // given
        Wallet wallet = myWalletRepository.findByMemberId(member.getId()).orElseThrow();
        linkCardRepository.save(createMyWalletCard(1L, wallet, member, UNREGISTERED));

        // when
        LinkCardsResponse response = linkCardService.read(member.getId(), null, 10, "owned");

        // then
        assertThat(response.linkCards()).hasSize(1);
    }

    @Test
    void 보유한_링크카드_중_링크지갑카드를_조회한다() {
        // given
        Member member1 = createMember("test@email.com", "u1");

        memberRepository.saveAll(List.of(member1));
        linkedWalletRepository.save(LinkedWallet.builder().id(1L).name("링크지갑").build());
        LinkedWallet linkedWallet = linkedWalletRepository.findById(1L).orElseThrow();
        linkedMemberRepository.saveAll(List.of(
                createLinkedMember(CREATOR, member, linkedWallet),
                createLinkedMember(PARTICIPANT, member1, linkedWallet)));
        linkCardRepository.saveAll(
                List.of(createLinkWalletCard(linkedWallet, member),
                        createLinkWalletCard(linkedWallet, member1)));

        // when
        LinkCardsResponse response = linkCardService.read(member.getId(), null, 10, "linked");

        // then
        assertThat(response.linkCards()).hasSize(1);
    }

    @Test
    void 공유한_링크카드를_조회한다() {
        // given
        Member member1 = createMember("test@email.com", "u1");
        Member member2 = createMember("test2@email.com", "u2");
        Member member3 = createMember("test3@email.com", "u3");

        memberRepository.saveAll(List.of(member1, member2, member3));
        linkedWalletRepository.save(LinkedWallet.builder().id(1L).name("링크지갑").build());
        LinkedWallet linkedWallet = linkedWalletRepository.findById(1L).orElseThrow();
        linkedMemberRepository.saveAll(List.of(
                createLinkedMember(CREATOR, member, linkedWallet),
                createLinkedMember(PARTICIPANT, member1, linkedWallet),
                createLinkedMember(PARTICIPANT, member2, linkedWallet),
                createLinkedMember(PARTICIPANT, member3, linkedWallet)));
        linkCardRepository.saveAll(
                List.of(createLinkWalletCard(linkedWallet, member1),
                        createLinkWalletCard(linkedWallet, member2),
                        createLinkWalletCard(linkedWallet, member3)));

        // when
        LinkCardsResponse response = linkCardService.read(member.getId(), null, 10, "shared");

        // then
        assertThat(response.linkCards()).hasSize(3);
    }

    @Test
    void 보유한_링크카드_중_등록안된_카드를_조회한다() {
        // given
        Wallet wallet = myWalletRepository.findByMemberId(member.getId()).orElseThrow();
        linkCardRepository.save(createMyWalletCard(1L, wallet, member, UNREGISTERED));

        // when
        LinkCardsResponse response = linkCardService.readByState(member.getId(), null, 10, UNREGISTERED,
                now());

        // then
        assertThat(response.linkCards()).hasSize(1);
    }

    @Test
    void 보유한_링크카드_중_일부를_결제카드로_등록한다() {
        // given
        Wallet wallet = myWalletRepository.findByMemberId(member.getId()).orElseThrow();
        linkCardRepository.save(createMyWalletCard(1L, wallet, member, UNREGISTERED));

        List<LinkCard> cards =
                linkCardRepository.findByMemberId(member.getId())
                        .stream()
                        .filter(card -> card.getState().equals(UNREGISTERED)).toList();

        // when
        linkCardService.activateLinkCard(
                List.of(cards.getFirst().getId()), member.getId());

        // then
        List<LinkCard> result = linkCardRepository.findByMemberId(member.getId());
        assertThat(result.getFirst().getState()).isEqualTo(REGISTERED);
    }

    @Test
    void 내가_보유한_링크카드가_아닌카드를_결제카드로_등록_시도시_오류가_발생한다() {
        // given
        Member member1 = createMember("test@email.com", "u1");
        memberRepository.save(member1);
        myWalletRepository.save(MyWallet.builder()
                .id(idGenerator.generate())
                .member(member1)
                .build());

        Wallet wallet = myWalletRepository.findByMemberId(member.getId()).orElseThrow();
        Wallet otherWallet = myWalletRepository.findByMemberId(member1.getId()).orElseThrow();

        linkCardRepository.saveAll(List.of(createMyWalletCard(1L, wallet, member, UNREGISTERED),
                createMyWalletCard(2L, otherWallet, member1, UNREGISTERED)));

        List<LinkCard> cards =
                linkCardRepository.findByMemberId(member.getId())
                        .stream()
                        .filter(card -> card.getState().equals(UNREGISTERED)).toList();

        // when //then
        assertThatThrownBy(() -> linkCardService.activateLinkCard(List.of(1L, 2L), member.getId())).isInstanceOf(
                LinkPayException.class).hasMessage("소유하지 않은 카드 아이디입니다.");

    }

    @Test
    void 결제카드로_등록된_링크카드를_조회한다() {
        // given
        Wallet wallet = myWalletRepository.findByMemberId(member.getId()).orElseThrow();
        linkCardRepository.save(createMyWalletCard(1L, wallet, member, REGISTERED));

        // when
        LinkCardsResponse registeredCards = linkCardService.readByState(member.getId(), null, 10, REGISTERED,
                now());

        // then
        assertThat(registeredCards.linkCards()).hasSize(1);
    }

    @Test
    void 등록여부에_따른_카드검색시_만료일이_지난_카드는_조회되지_않는다() {
        // given
        Wallet wallet = myWalletRepository.findByMemberId(member.getId()).orElseThrow();
        linkCardRepository.save(createMyWalletCard(1L, wallet, member, UNREGISTERED));

        // when
        LinkCardsResponse response = linkCardService.readByState(member.getId(), null, 10, UNREGISTERED,
                now().plusDays(1));

        // then
        assertThat(response.linkCards()).isEmpty();
        List<LinkCard> result = linkCardRepository.findByMemberId(member.getId());
        assertThat(result).hasSize(1);
    }


    @Test
    void 내_지갑_링크카드를_삭제한다() {
        // given
        Wallet wallet = myWalletRepository.findByMemberId(member.getId()).orElseThrow();
        linkCardRepository.save(createMyWalletCard(1L, wallet, member, UNREGISTERED));

        // when
        linkCardService.deleteLinkCard(1L, member.getId());

        // then
        assertThat(linkCardRepository.findByMemberId(member.getId()).size()).isEqualTo(0);
    }

    @Test
    void 링크_지갑_링크카드를_삭제한다() {
        // given
        Member member1 = createMember("test@email.com", "u1");

        memberRepository.saveAll(List.of(member1));
        linkedWalletRepository.save(LinkedWallet.builder().id(1L).name("링크지갑").build());
        LinkedWallet linkedWallet = linkedWalletRepository.findById(1L).orElseThrow();
        linkedMemberRepository.saveAll(List.of(
                LinkedMember.builder().id(1L).role(CREATOR).member(member).linkedWallet(linkedWallet).build(),
                LinkedMember.builder().id(2L).role(PARTICIPANT).member(member1).linkedWallet(linkedWallet)
                        .build()));

        LinkCard card = createLinkWalletCard(linkedWallet, member);
        linkCardRepository.saveAll(
                List.of(card,
                        createLinkWalletCard(linkedWallet, member1)));

        // when
        linkCardService.deleteLinkCard(card.getId(), member.getId());

        // then
        assertThat(linkCardRepository.findByMemberId(member.getId()).size()).isEqualTo(0);
    }

    @Test
    void 링크지갑_공유한_카드를_삭제한다() {
        // given
        Member member1 = createMember("test@email.com", "u1");

        memberRepository.saveAll(List.of(member1));
        linkedWalletRepository.save(LinkedWallet.builder().id(1L).name("링크지갑").build());
        LinkedWallet linkedWallet = linkedWalletRepository.findById(1L).orElseThrow();
        linkedMemberRepository.saveAll(List.of(
                LinkedMember.builder().id(1L).role(CREATOR).member(member).linkedWallet(linkedWallet).build(),
                LinkedMember.builder().id(2L).role(PARTICIPANT).member(member1).linkedWallet(linkedWallet)
                        .build()));

        LinkCard card = createLinkWalletCard(linkedWallet, member1);
        linkCardRepository.saveAll(
                List.of(createLinkWalletCard(linkedWallet, member),
                        card));

        // when
        linkCardService.deleteLinkCard(card.getId(), member1.getId());

        // then
        assertThat(linkCardRepository.findByMemberId(member1.getId()).size()).isEqualTo(0);
    }

    @Test
    void 내_지갑_링크카드를_상세조회한다() {
        // given
        Wallet wallet = myWalletRepository.findByMemberId(member.getId()).orElseThrow();
        linkCardRepository.save(createMyWalletCard(1L, wallet, member, UNREGISTERED));

        // when
        LinkCardDetailResponse response = linkCardService.getLinkCardDetails(member.getId(), 1L);

        // then
        assertThat(response).isNotNull();
    }

    @Test
    void 링크지갑_링크카드를_상세조회한다() {
        // given
        Member member1 = createMember("test@email.com", "u1");
        String walletName = "링크지갑";

        memberRepository.saveAll(List.of(member1));
        linkedWalletRepository.save(LinkedWallet.builder().id(1L).name(walletName).build());
        LinkedWallet linkedWallet = linkedWalletRepository.findById(1L).orElseThrow();
        linkedMemberRepository.saveAll(List.of(
                createLinkedMember(CREATOR, member, linkedWallet),
                createLinkedMember(PARTICIPANT, member1, linkedWallet)));

        LinkCard card = createLinkWalletCard(linkedWallet, member);
        linkCardRepository.saveAll(
                List.of(card,
                        createLinkWalletCard(linkedWallet, member1)));

        // when
        LinkCardDetailResponse response = linkCardService.getLinkCardDetails(member.getId(), card.getId());

        // then
        assertAll(
                () -> assertThat(response).isNotNull(),
                () -> assertThat(response.walletName()).isEqualTo(walletName)
        );

    }

    @Test
    void 카드_사용내역을_조회한다() {
        // given
        Wallet wallet = myWalletRepository.findByMemberId(member.getId()).orElseThrow();
        LinkCard linkCard = linkCardRepository.save(createMyWalletCard(1L, wallet, member, UNREGISTERED));
        Store store = storeRepository.save(Store.builder().id(1L).name("store").build());
        wallet.chargePoint(new Point(50000L));
        paymentRepository.save(createPayment(linkCard, store));

        // when
        LinkCardHistoriesResponse res = linkCardService.getLinkCardHistories(member.getId(), null, 10,
                linkCard.getId());

        // then
        assertAll(
                () -> assertThat(res.linkCardHistories().size()).isEqualTo(1),
                () -> assertThat(res.linkCardHistories().getFirst().linkCardId()).isEqualTo(linkCard.getId().toString())
        );
    }

    private Payment createPayment(final LinkCard linkCard, final Store store) {
        return Payment.builder()
                .id(1L)
                .linkCard(linkCard)
                .amount(new Point(3000L))
                .member(member).store(store)
                .build();
    }

    private static LinkCardCreateServiceRequest createCard(final LocalDate date) {
        return LinkCardCreateServiceRequest.builder()
                .cardName("test card")
                .expiredAt(date)
                .limitPrice(new Point(500000))
                .build();
    }

    private static SharedLinkCardCreateServiceRequest createSharedCards(final LocalDate date, final String walletId,
                                                                        final List<String> memberIds) {
        return SharedLinkCardCreateServiceRequest.builder()
                .cardName("test card")
                .expiredAt(date)
                .limitPrice(new Point(500000))
                .memberIds(memberIds)
                .linkedWalletId(walletId)
                .build();
    }


    private static LinkCard createMyWalletCard(final Long id, final Wallet wallet, final Member member,
                                               final CardState cardState) {
        return LinkCard.builder()
                .id(id)
                .cardColor(CardColor.getRandomColor())
                .cardName("cardName")
                .cardType(OWNED)
                .wallet(wallet)
                .limitPrice(new Point(50000000))
                .member(member)
                .expiredAt(LocalDate.now().plusDays(1).atStartOfDay())
                .usedPoint(new Point(0))
                .state(cardState)
                .build();
    }
}
