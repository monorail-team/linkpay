package monorail.linkpay.linkcard;

import static java.time.LocalDateTime.now;
import static monorail.linkpay.linkcard.domain.CardState.REGISTERED;
import static monorail.linkpay.linkcard.domain.CardState.UNREGISTERED;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import monorail.linkpay.common.IntegrationTest;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.linkcard.dto.LinkCardsResponse;
import monorail.linkpay.linkcard.service.LinkCardService;
import monorail.linkpay.linkcard.service.request.LinkCardCreateServiceRequest;
import monorail.linkpay.linkcard.service.request.SharedLinkCardCreateServiceRequest;
import monorail.linkpay.wallet.dto.LinkedWalletsResponse;
import monorail.linkpay.wallet.service.LinkedWalletService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class LinkCardServiceTest extends IntegrationTest {

    @Autowired
    private LinkCardService linkCardService;
    @Autowired
    private LinkedWalletService linkedWalletService;

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
        linkedWalletService.createLinkedWallet(member.getId(), "링크지갑", Set.of());
        LinkedWalletsResponse walletRes = linkedWalletService.readLinkedWallets(member.getId(), null, 10);
        SharedLinkCardCreateServiceRequest request = createSharedCard(LocalDate.now().plusDays(1),
                Long.parseLong(walletRes.linkedWallets().getFirst().linkedWalletId()), member.getId());

        // when
        linkCardService.createShared(request);

        List<LinkCard> result = linkCardRepository.findLinkCardsByMember(member);
        assertThat(result).isNotNull();
    }

    @Test
    void 보유한_링크카드를_조회한다() {
        // given
        LinkCardCreateServiceRequest request = createCard(LocalDate.now().plusDays(1));
        linkCardService.create(member.getId(), request);

        // when
        LinkCardsResponse response = linkCardService.read(member.getId(), null, 10);

        // then
        assertThat(response.linkCards()).hasSize(1);
    }

    @Test
    void 보유한_링크카드_중_등록안된_카드를_조회한다() {
        // given
        LinkCardCreateServiceRequest request = createCard(LocalDate.now().plusDays(1));
        linkCardService.create(member.getId(), request);

        // when
        LinkCardsResponse response = linkCardService.readByState(member.getId(), null, 10, UNREGISTERED,
                now());

        // then
        assertThat(response.linkCards()).hasSize(1);
    }

    @Test
    void 보유한_링크카드_중_일부를_결제카드로_등록한다() {
        // given
        LinkCardCreateServiceRequest request = createCard(LocalDate.now().plusDays(1));
        linkCardService.create(member.getId(), request);
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
        LinkCardCreateServiceRequest request = createCard(LocalDate.now().plusDays(1));
        linkCardService.create(member.getId(), request);
        LinkCardsResponse unregisteredCards = linkCardService.readByState(member.getId(), null, 10, UNREGISTERED,
                now());
        linkCardService.activateLinkCard(
                List.of(Long.parseLong(unregisteredCards.linkCards().getFirst().linkCardId())));

        // when
        LinkCardsResponse registeredCards = linkCardService.readByState(member.getId(), null, 10, REGISTERED,
                now());

        // then
        assertThat(registeredCards.linkCards()).hasSize(1);
    }

    @Test
    void 등록여부에_따른_카드검색시_만료일이_지난_카드는_조회되지_않는다() {
        // given
        LinkCardCreateServiceRequest request = createCard(LocalDate.now());
        linkCardService.create(member.getId(), request);

        // when
        LinkCardsResponse response = linkCardService.readByState(member.getId(), null, 10, UNREGISTERED,
                now().plusDays(1));

        // then
        assertThat(response.linkCards()).isEmpty();
        List<LinkCard> result = linkCardRepository.findLinkCardsByMember(member);
        assertThat(result).hasSize(1);
    }

    private static LinkCardCreateServiceRequest createCard(LocalDate date) {
        return LinkCardCreateServiceRequest.builder()
                .cardName("test card")
                .expiredAt(date)
                .limitPrice(new Point(500000))
                .build();
    }

    private static SharedLinkCardCreateServiceRequest createSharedCard(LocalDate date, long walletId, long memberId) {
        return SharedLinkCardCreateServiceRequest.builder()
                .cardName("test card")
                .expiredAt(date)
                .limitPrice(new Point(500000))
                .memberIds(List.of(memberId))
                .linkedWalletId(walletId)
                .build();
    }
}
