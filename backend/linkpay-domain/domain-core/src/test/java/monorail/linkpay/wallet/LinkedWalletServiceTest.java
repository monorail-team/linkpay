package monorail.linkpay.wallet;

import static monorail.linkpay.exception.ExceptionCode.INVALID_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Set;
import monorail.linkpay.common.IntegrationTest;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.linkcard.LinkCardServiceTest;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.wallet.domain.LinkedWallet;
import monorail.linkpay.wallet.dto.LinkedWalletResponse;
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
    void 링크지갑을_조회한다() {
        // given
        Member member1 = memberRepository.save(createMember("lion@gmail.com", "lion"));
        Member member2 = memberRepository.save(createMember("tiger@gmail.com", "tiger"));
        Long linkedWalletId = linkedWalletService.createLinkedWallet(member.getId(), "animal",
                Set.of(member1.getId(), member2.getId()));

        // when
        LinkedWalletResponse linkedWalletResponse = linkedWalletService.readLinkedWallet(linkedWalletId);

        // then
        assertThat(linkedWalletResponse)
                .extracting("linkedWalletId", "linkedWalletName", "participantCount")
                .contains(linkedWalletId.toString(), "animal", 3);
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
        assertThat(linkedWalletService.readLinkedWallet(linkedWalletId))
                .extracting("linkedWalletId", "linkedWalletName", "amount")
                .contains(linkedWalletId.toString(), "animal", 10000L);
    }

    @Test
    void 링크지갑에서_현재_잔액보다_많은_금액을_차감하면_예외가_발생한다() {
        // given
        Member member1 = memberRepository.save(createMember("lion@gmail.com", "lion"));
        Member member2 = memberRepository.save(createMember("tiger@gmail.com", "tiger"));
        Long linkedWalletId = linkedWalletService.createLinkedWallet(member.getId(), "animal",
                Set.of(member1.getId(), member2.getId()));
        linkedWalletService.chargeLinkedWallet(linkedWalletId, new Point(10000), member1.getId());

        // when, then
        assertThatThrownBy(() ->
                linkedWalletService.deductLinkedWallet(linkedWalletId, new Point(20000), member1.getId()))
                .isInstanceOf(LinkPayException.class)
                .hasMessageContaining("차감할 금액은 잔액보다 작거나 같은 값이어야 합니다.")
                .extracting("exceptionCode")
                .isEqualTo(INVALID_REQUEST);
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
        assertThatThrownBy(() -> linkedWalletService.deleteLinkedWallet(linkedWalletId))
                .isInstanceOf(LinkPayException.class)
                .hasMessageContaining("해당 지갑에 링크카드가 존재합니다.")
                .extracting("exceptionCode")
                .isEqualTo(INVALID_REQUEST);
    }
}
