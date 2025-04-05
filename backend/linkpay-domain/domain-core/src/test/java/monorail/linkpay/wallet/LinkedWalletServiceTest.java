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
    void 링크지갑_생성_시_회원이_아닌_아이디값을_보내면_오류가_발생한다() {
        // given
        Member member1 = memberRepository.save(createMember("lion@gmail.com", "lion"));
        Member member2 = memberRepository.save(createMember("tiger@gmail.com", "tiger"));

        // when // then
        assertThatThrownBy(() -> linkedWalletService.createLinkedWallet(member.getId(), "animal",
                Set.of(member1.getId(), member2.getId(), 999L))).isInstanceOf(LinkPayException.class)
                .hasMessage("존재하지 않는 회원 아이디가 포함되어있습니다.");
    }

    @Test
    void 링크지갑_하나를_조회한다() {
        // given
        Member member1 = memberRepository.save(createMember("lion@gmail.com", "lion"));
        Member member2 = memberRepository.save(createMember("tiger@gmail.com", "tiger"));

        LinkedWallet linkedWallet = createLinkedWallet("animal", Set.of(member1, member2));

        // when
        LinkedWalletResponse linkedWalletResponse = linkedWalletService.readLinkedWallet(linkedWallet.getId(),
                member.getId());

        // then
        assertThat(linkedWalletResponse)
                .extracting("linkedWalletId", "linkedWalletName", "amount", "participantCount")
                .contains(linkedWallet.getId().toString(), "animal", 0L, 3);
    }

    @Test
    void 보유한_모든_링크지갑을_조회한다() {
        // given
        Member member1 = memberRepository.save(createMember("lion@gmail.com", "lion"));
        Member member2 = memberRepository.save(createMember("tiger@gmail.com", "tiger"));
        LinkedWallet linkedWallet1 = createLinkedWallet("animal-1", Set.of(member1, member2));
        LinkedWallet linkedWallet2 = createLinkedWallet("animal-2", Set.of(member1, member2));

        // when
        LinkedWalletsResponse linkedWalletsResponse = linkedWalletService.readLinkedWallets(
                member.getId(), CREATOR, null, 10);

        // then
        assertThat(linkedWalletsResponse.linkedWallets()).hasSize(2)
                .extracting("linkedWalletId", "linkedWalletName", "participantCount", "amount")
                .containsExactlyInAnyOrder(
                        tuple(linkedWallet1.getId().toString(), "animal-1", 3, 0L),
                        tuple(linkedWallet2.getId().toString(), "animal-2", 3, 0L)
                );
    }

    @Test
    void 참여한_모든_링크지갑을_조회한다() {
        // given
        Member member1 = memberRepository.save(createMember("lion@gmail.com", "lion"));
        Member member2 = memberRepository.save(createMember("tiger@gmail.com", "tiger"));
        createLinkedWallet("animal-1", Set.of(member1));
        createLinkedWallet("animal-2", Set.of(member1, member2));

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
        LinkedWallet linkedWallet = createLinkedWallet("animal", Set.of(member1, member2));

        // when
        linkedWalletService.chargeLinkedWallet(linkedWallet.getId(), new Point(10000), member1.getId());

        // then
        assertThat(linkedWalletService.readLinkedWallet(linkedWallet.getId(), member.getId()))
                .extracting("linkedWalletId", "linkedWalletName", "amount", "participantCount")
                .contains(linkedWallet.getId().toString(), "animal", 10000L, 3);
    }

    @Test
    void 링크지갑을_삭제할_때_지갑과_연동된_링크카드가_하나라도_존재하면_예외가_발생한다() {
        // given
        Member member1 = memberRepository.save(createMember("lion@gmail.com", "lion"));
        Member member2 = memberRepository.save(createMember("tiger@gmail.com", "tiger"));
        LinkedWallet linkedWallet = createLinkedWallet("animal", Set.of(member1, member2));
        linkCardRepository.save(createLinkWalletCard(
                linkedWallet, member1));

        // when, then
        assertThatThrownBy(() -> linkedWalletService.deleteLinkedWallet(linkedWallet.getId(), member.getId()))
                .isInstanceOf(LinkPayException.class)
                .hasMessageContaining("해당 지갑에 링크카드가 존재합니다.")
                .extracting("exceptionCode")
                .isEqualTo(INVALID_REQUEST);
    }

    @Test
    void 소유한_링크지갑의_이름을_변경한다() {
        // given
        Member member1 = memberRepository.save(createMember("lion@gmail.com", "lion"));
        LinkedWallet linkedWallet = createLinkedWallet("testname", Set.of(member1));

        // when
        linkedWalletService.changeLinkedWallet(linkedWallet.getId(), "새로운이름", member.getId());

        // then
        assertThat(linkedWalletService.readLinkedWallet(linkedWallet.getId(), member.getId())
                .linkedWalletName()).isEqualTo(
                "새로운이름");
    }

    @Test
    void 링크지갑의_참여자가_이름을_변경시도하면_예외가_발생한다() {
        // given
        Member member1 = memberRepository.save(createMember("lion@gmail.com", "lion"));
        LinkedWallet linkedWallet = createLinkedWallet("testname", Set.of(member1));

        // when // then
        assertThatThrownBy(() -> linkedWalletService.changeLinkedWallet(linkedWallet.getId(), "새로운이름",
                member1.getId())).isInstanceOf(LinkPayException.class);
    }

    @Test
    void 링크지갑의_생성자나_참여자가_아닌_사람이_이름변경시도하면_예외가_발생한다() {
        // given
        Member member1 = memberRepository.save(createMember("lion@gmail.com", "lion"));
        Member member2 = memberRepository.save(createMember("lion2@gmail.com", "lion2"));

        LinkedWallet linkedWallet = createLinkedWallet("testname", Set.of(member1));

        // when // then
        assertThatThrownBy(() -> linkedWalletService.changeLinkedWallet(linkedWallet.getId(), "새로운이름",
                member2.getId())).isInstanceOf(LinkPayException.class);
    }

    private LinkedWallet createLinkedWallet(String name, Set<Member> members) {
        LinkedWallet linkedWallet = linkedWalletRepository.save(createLinkedWallet(name));
        linkedMemberRepository.save((createLinkedMember(CREATOR, member, linkedWallet)));
        linkedMemberRepository.saveAll(
                members.stream().map(member -> createLinkedMember(PARTICIPANT, member, linkedWallet)).toList());

        return linkedWallet;
    }

}
