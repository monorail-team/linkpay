package monorail.linkpay.linkcard;

import static java.time.LocalDateTime.now;
import static monorail.linkpay.linkcard.domain.CardState.REGISTERED;
import static monorail.linkpay.linkcard.domain.CardState.UNREGISTERED;
import static monorail.linkpay.linkcard.domain.CardType.OWNED;
import static monorail.linkpay.linkcard.domain.CardType.SHARED;
import static monorail.linkpay.wallet.domain.Role.CREATOR;
import static monorail.linkpay.wallet.domain.Role.PARTICIPANT;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import monorail.linkpay.common.IntegrationTest;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.linkcard.domain.CardColor;
import monorail.linkpay.linkcard.domain.CardState;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.linkcard.dto.LinkCardsResponse;
import monorail.linkpay.linkcard.service.LinkCardService;
import monorail.linkpay.linkcard.service.request.LinkCardCreateServiceRequest;
import monorail.linkpay.linkcard.service.request.SharedLinkCardCreateServiceRequest;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.wallet.domain.LinkedMember;
import monorail.linkpay.wallet.domain.LinkedWallet;
import monorail.linkpay.wallet.domain.Role;
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

        List<LinkCard> result = linkCardRepository.findLinkCardsByMember(member);
        assertThat(result).isNotNull();
    }

    @Test
    void 링크지갑에서_카드를_생성한다() {
        // given
        Member member1 = createMember(1L, "test@email.com", "u1");
        memberRepository.saveAll(List.of(member1));
        linkedWalletRepository.save(LinkedWallet.builder().id(1L).name("링크지갑").build());
        LinkedWallet linkedWallet = linkedWalletRepository.findById(1L).orElseThrow();
        linkedMemberRepository.saveAll(List.of(
                createLinkedMember(1L, CREATOR, member, linkedWallet),
                createLinkedMember(2L, PARTICIPANT, member1, linkedWallet)));
        SharedLinkCardCreateServiceRequest request = createSharedCards(LocalDate.now().plusDays(1),
                linkedWallet.getId(),
                List.of(member.getId(), 1L, 2L, 3L));

        // when
        linkCardService.createShared(request);

        List<LinkCard> result = linkCardRepository.findLinkCardsByMember(member);
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
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
        Member member1 = createMember(1L, "test@email.com", "u1");

        memberRepository.saveAll(List.of(member1));
        linkedWalletRepository.save(LinkedWallet.builder().id(1L).name("링크지갑").build());
        LinkedWallet linkedWallet = linkedWalletRepository.findById(1L).orElseThrow();
        linkedMemberRepository.saveAll(List.of(
                createLinkedMember(1L, CREATOR, member, linkedWallet),
                createLinkedMember(2L, PARTICIPANT, member1, linkedWallet)));
        linkCardRepository.saveAll(
                List.of(createLinkWalletCard(1L, linkedWallet, member),
                        createLinkWalletCard(2L, linkedWallet, member1)));

        // when
        LinkCardsResponse response = linkCardService.read(member.getId(), null, 10, "linked");

        // then
        assertThat(response.linkCards()).hasSize(1);
    }

    @Test
    void 공유한_링크카드를_조회한다() {
        // given
        Member member1 = createMember(1L, "test@email.com", "u1");
        Member member2 = createMember(2L, "test2@email.com", "u2");
        Member member3 = createMember(3L, "test3@email.com", "u3");

        memberRepository.saveAll(List.of(member1, member2, member3));
        linkedWalletRepository.save(LinkedWallet.builder().id(1L).name("링크지갑").build());
        LinkedWallet linkedWallet = linkedWalletRepository.findById(1L).orElseThrow();
        linkedMemberRepository.saveAll(List.of(
                createLinkedMember(1L, CREATOR, member, linkedWallet),
                createLinkedMember(2L, PARTICIPANT, member1, linkedWallet),
                createLinkedMember(3L, PARTICIPANT, member2, linkedWallet),
                createLinkedMember(4L, PARTICIPANT, member3, linkedWallet)));
        linkCardRepository.saveAll(
                List.of(createLinkWalletCard(1L, linkedWallet, member1),
                        createLinkWalletCard(2L, linkedWallet, member2),
                        createLinkWalletCard(3L, linkedWallet, member3)));

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
        LinkCardsResponse unregisteredCards = linkCardService.readByState(member.getId(), null, 10, UNREGISTERED,
                now());

        // when
        linkCardService.activateLinkCard(
                List.of(Long.parseLong(unregisteredCards.linkCards().getFirst().linkCardId())));

        // then
        List<LinkCard> result = linkCardRepository.findLinkCardsByMember(member);
        assertThat(result.getFirst().getState()).isEqualTo(REGISTERED);
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
        List<LinkCard> result = linkCardRepository.findLinkCardsByMember(member);
        assertThat(result).hasSize(1);
    }

    private LinkedMember createLinkedMember(long id, Role creator, Member member, LinkedWallet linkedWallet) {
        return LinkedMember.builder()
                .id(id)
                .role(creator)
                .member(member)
                .linkedWallet(linkedWallet)
                .build();
    }

    @Test
    void 내_지갑_링크카드를_삭제한다() {
        // given
        Wallet wallet = myWalletRepository.findByMemberId(member.getId()).orElseThrow();
        linkCardRepository.save(createMyWalletCard(1L, wallet, member, UNREGISTERED));

        // when
        linkCardService.deleteLinkCard(1L, member.getId());

        // then
        assertThat(linkCardRepository.findLinkCardsByMember(member).size()).isEqualTo(0);
    }

    @Test
    void 링크_지갑_링크카드를_삭제한다() {
        // given
        Member member1 = createMember(1L, "test@email.com", "u1");

        memberRepository.saveAll(List.of(member1));
        linkedWalletRepository.save(LinkedWallet.builder().id(1L).name("링크지갑").build());
        LinkedWallet linkedWallet = linkedWalletRepository.findById(1L).orElseThrow();
        linkedMemberRepository.saveAll(List.of(
                LinkedMember.builder().id(1L).role(CREATOR).member(member).linkedWallet(linkedWallet).build(),
                LinkedMember.builder().id(2L).role(PARTICIPANT).member(member1).linkedWallet(linkedWallet)
                        .build()));
        linkCardRepository.saveAll(
                List.of(createLinkWalletCard(1L, linkedWallet, member),
                        createLinkWalletCard(2L, linkedWallet, member1)));

        // when
        linkCardService.deleteLinkCard(1L, member.getId());

        // then
        assertThat(linkCardRepository.findLinkCardsByMember(member).size()).isEqualTo(0);
    }

    @Test
    void 링크지갑_공유한_카드를_삭제한다() {
        // given
        Member member1 = createMember(1L, "test@email.com", "u1");

        memberRepository.saveAll(List.of(member1));
        linkedWalletRepository.save(LinkedWallet.builder().id(1L).name("링크지갑").build());
        LinkedWallet linkedWallet = linkedWalletRepository.findById(1L).orElseThrow();
        linkedMemberRepository.saveAll(List.of(
                LinkedMember.builder().id(1L).role(CREATOR).member(member).linkedWallet(linkedWallet).build(),
                LinkedMember.builder().id(2L).role(PARTICIPANT).member(member1).linkedWallet(linkedWallet)
                        .build()));
        linkCardRepository.saveAll(
                List.of(createLinkWalletCard(1L, linkedWallet, member),
                        createLinkWalletCard(2L, linkedWallet, member1)));

        // when
        linkCardService.deleteLinkCard(2L, member1.getId());

        // then
        assertThat(linkCardRepository.findLinkCardsByMember(member1).size()).isEqualTo(0);
    }

    private static LinkCardCreateServiceRequest createCard(LocalDate date) {
        return LinkCardCreateServiceRequest.builder()
                .cardName("test card")
                .expiredAt(date)
                .limitPrice(new Point(500000))
                .build();
    }

    private static SharedLinkCardCreateServiceRequest createSharedCards(LocalDate date, long walletId,
                                                                        List<Long> memberIds) {
        return SharedLinkCardCreateServiceRequest.builder()
                .cardName("test card")
                .expiredAt(date)
                .limitPrice(new Point(500000))
                .memberIds(memberIds)
                .linkedWalletId(walletId)
                .build();
    }

    private static Member createMember(long id, String mail, String username) {
        return Member.builder()
                .id(id)
                .email(mail)
                .username(username)
                .build();
    }

    public static LinkCard createLinkWalletCard(Long id, LinkedWallet linkedWallet, Member member) {
        return LinkCard.builder()
                .id(id)
                .cardColor(CardColor.getRandomColor())
                .cardName("cardName")
                .cardType(SHARED)
                .wallet(linkedWallet)
                .limitPrice(new Point(50000000))
                .member(member)
                .expiredAt(LocalDate.now().plusDays(1).atStartOfDay())
                .usedPoint(new Point(0))
                .state(UNREGISTERED)
                .build();
    }

    private static LinkCard createMyWalletCard(Long id, Wallet wallet, Member member, CardState cardState) {
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
