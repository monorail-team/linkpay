package monorail.linkpay.wallet;

import static monorail.linkpay.exception.ExceptionCode.INVALID_REQUEST;
import static monorail.linkpay.wallet.domain.Role.CREATOR;
import static monorail.linkpay.wallet.domain.Role.PARTICIPANT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.util.Set;
import monorail.linkpay.common.IntegrationTest;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.linkcard.LinkCardServiceTest;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.wallet.domain.LinkedWallet;
import monorail.linkpay.wallet.dto.LinkedWalletResponse;
import monorail.linkpay.wallet.dto.LinkedWalletsResponse;
import monorail.linkpay.wallet.service.LinkedWalletService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class LinkedWalletServiceTest extends IntegrationTest {

    @Autowired
    private LinkedWalletService linkedWalletService;

    @Test
    void 링크지갑을_생성한다() {
        // given
        Member member1 = memberRepository.save(createMember("lion@gmail.com", "lion"));
        Member member2 = memberRepository.save(createMember("tiger@gmail.com", "tiger"));

        // when
        Long linkedWalletId = linkedWalletService.createLinkedWallet(member.getId(), "animal",
                Set.of(member1.getId(), member2.getId()));

        // then
        assertThat(linkedWalletId).isNotNull();
    }

    @Test
    void 링크지갑_하나를_조회한다() {
        // given
        Member member1 = memberRepository.save(createMember("lion@gmail.com", "lion"));
        Member member2 = memberRepository.save(createMember("tiger@gmail.com", "tiger"));
        Long linkedWalletId = linkedWalletService.createLinkedWallet(member.getId(), "animal",
                Set.of(member1.getId(), member2.getId()));

        // when
        LinkedWalletResponse linkedWalletResponse = linkedWalletService.readLinkedWallet(linkedWalletId,
                member.getId());

        // then
        assertThat(linkedWalletResponse)
                .extracting("linkedWalletId", "linkedWalletName", "amount", "participantCount")
                .contains(linkedWalletId.toString(), "animal", 0L, 3);
    }

    @Test
    void 보유한_모든_링크지갑을_조회한다() {
        // given
        Member member1 = memberRepository.save(createMember("lion@gmail.com", "lion"));
        Member member2 = memberRepository.save(createMember("tiger@gmail.com", "tiger"));
        Long linkedWalletId1 = linkedWalletService.createLinkedWallet(member.getId(), "animal-1",
                Set.of(member1.getId(), member2.getId()));
        Long linkedWalletId2 = linkedWalletService.createLinkedWallet(member.getId(), "animal-2",
                Set.of(member1.getId(), member2.getId()));

        // when
        LinkedWalletsResponse linkedWalletsResponse = linkedWalletService.readLinkedWallets(
                member.getId(), CREATOR, null, 10);

        // then
        assertThat(linkedWalletsResponse.linkedWallets()).hasSize(2)
                .extracting("linkedWalletId", "linkedWalletName", "participantCount", "amount")
                .containsExactlyInAnyOrder(
                        tuple(linkedWalletId1.toString(), "animal-1", 3, 0L),
                        tuple(linkedWalletId2.toString(), "animal-2", 3, 0L)
                );
    }

    @Test
    void 참여한_모든_링크지갑을_조회한다() {
        // given
        Member member1 = memberRepository.save(createMember("lion@gmail.com", "lion"));
        Member member2 = memberRepository.save(createMember("tiger@gmail.com", "tiger"));
        Long linkedWalletId1 = linkedWalletService.createLinkedWallet(member.getId(), "animal-1",
                Set.of(member1.getId()));
        Long linkedWalletId2 = linkedWalletService.createLinkedWallet(member.getId(), "animal-2",
                Set.of(member1.getId(), member2.getId()));

        // when
        LinkedWalletsResponse linkedWalletsResponse = linkedWalletService.readLinkedWallets(
                member.getId(), PARTICIPANT, null, 10);

        // then
        assertThat(linkedWalletsResponse.linkedWallets()).isEmpty();
    }

    @Test
    void 링크지갑에_포인트를_충전한다() {
        // given
        Member member1 = memberRepository.save(createMember("lion@gmail.com", "lion"));
        Member member2 = memberRepository.save(createMember("tiger@gmail.com", "tiger"));
        Long linkedWalletId = linkedWalletService.createLinkedWallet(member.getId(), "animal",
                Set.of(member1.getId(), member2.getId()));

        // when
        linkedWalletService.chargeLinkedWallet(linkedWalletId, new Point(10000), member1.getId());

        // then
        assertThat(linkedWalletService.readLinkedWallet(linkedWalletId, member.getId()))
                .extracting("linkedWalletId", "linkedWalletName", "amount", "participantCount")
                .contains(linkedWalletId.toString(), "animal", 10000L, 3);
    }

    @Test
    void 링크지갑을_삭제할_때_지갑과_연동된_링크카드가_하나라도_존재하면_예외가_발생한다() {
        // given
        Member member1 = memberRepository.save(createMember("lion@gmail.com", "lion"));
        Member member2 = memberRepository.save(createMember("tiger@gmail.com", "tiger"));
        Long linkedWalletId = linkedWalletService.createLinkedWallet(member.getId(), "animal",
                Set.of(member1.getId(), member2.getId()));
        linkedWalletService.chargeLinkedWallet(linkedWalletId, new Point(10000), member1.getId());
        linkCardRepository.save(LinkCardServiceTest.createLinkWalletCard(
                1L, LinkedWallet.builder().id(linkedWalletId).build(), member1));

        // when, then
        assertThatThrownBy(() -> linkedWalletService.deleteLinkedWallet(linkedWalletId, member.getId()))
                .isInstanceOf(LinkPayException.class)
                .hasMessageContaining("해당 지갑에 링크카드가 존재합니다.")
                .extracting("exceptionCode")
                .isEqualTo(INVALID_REQUEST);
    }
}
