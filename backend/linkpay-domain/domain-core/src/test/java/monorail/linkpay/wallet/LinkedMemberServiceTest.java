package monorail.linkpay.wallet;

import static monorail.linkpay.exception.ExceptionCode.INVALID_REQUEST;
import static monorail.linkpay.linkcard.LinkCardServiceTest.createSharedCards;
import static monorail.linkpay.wallet.domain.Role.CREATOR;
import static monorail.linkpay.wallet.domain.Role.PARTICIPANT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import monorail.linkpay.common.IntegrationTest;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.linkcard.service.LinkCardService;
import monorail.linkpay.linkcard.service.request.SharedLinkCardCreateServiceRequest;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.wallet.domain.LinkedMember;
import monorail.linkpay.wallet.domain.LinkedWallet;
import monorail.linkpay.wallet.domain.Role;
import monorail.linkpay.wallet.service.LinkedMemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class LinkedMemberServiceTest extends IntegrationTest {

    @Autowired
    private LinkedMemberService linkedMemberService;
    @Autowired
    private LinkCardService linkCardService;

    @Test
    void 지갑생성자가_아닌_사람이_링크멤버를_추가하면_예외가_발생한다() {
        // given
        Member member1 = memberRepository.save(createMember("lion@gmail.com", "lion"));
        Member member2 = memberRepository.save(createMember("tiger@gmail.com", "tiger"));
        Member member3 = memberRepository.save(createMember("bear@gmail.com", "bear"));
        LinkedWallet linkedWallet = linkedWalletRepository.save(getLinkWallet(1L, "link_wallet"));
        linkedMemberRepository.save(getLinkedMember(1L, CREATOR, linkedWallet, member1));
        linkedMemberRepository.save(getLinkedMember(2L, PARTICIPANT, linkedWallet, member2));

        // when // then
        assertThatThrownBy(() ->
                linkedMemberService.createLinkedMember(linkedWallet.getId(), member2.getId(), member3.getId()))
                .isInstanceOf(LinkPayException.class)
                .hasMessageContaining("지갑 생성자만 참여자를 관리할 수 있습니다.")
                .extracting("exceptionCode")
                .isEqualTo(INVALID_REQUEST);
    }

    @Test
    void 링크지갑에서_특정_링크멤버를_삭제하면_해당_링크멤버의_링크카드도_삭제한다() {
        Member member1 = memberRepository.save(createMember("lion@gmail.com", "lion"));
        Member member2 = memberRepository.save(createMember("tiger@gmail.com", "tiger"));
        LinkedWallet linkedWallet = linkedWalletRepository.save(getLinkWallet(1L, "link_wallet"));
        LinkedMember creator = linkedMemberRepository.save(getLinkedMember(1L, CREATOR, linkedWallet, member1));
        LinkedMember participant = linkedMemberRepository.save(getLinkedMember(2L, PARTICIPANT, linkedWallet, member2));
        SharedLinkCardCreateServiceRequest request = createSharedCards(LocalDate.now().plusDays(1),
                linkedWallet.getId().toString(),
                List.of(participant.getId().toString()));
        linkCardService.createShared(request, member1.getId());

        linkedMemberService.deleteLinkedMember(linkedWallet.getId(), participant.getId(), member1.getId());

        assertThat(linkCardRepository.findByMemberIdAndWalletId(participant.getId(), linkedWallet.getId())).isEmpty();
    }

    @Test
    void 링크지갑에서_링크멤버_여러명을_삭제하면_링크카드도_같이_삭제된다() {
        Member member1 = memberRepository.save(createMember("lion@gmail.com", "lion"));
        Member member2 = memberRepository.save(createMember("tiger@gmail.com", "tiger"));
        Member member3 = memberRepository.save(createMember("bear@gmail.com", "bear"));
        LinkedWallet linkedWallet = linkedWalletRepository.save(getLinkWallet(1L, "link_wallet"));
        LinkedMember creator = linkedMemberRepository.save(getLinkedMember(1L, CREATOR, linkedWallet, member1));
        LinkedMember participant1 = linkedMemberRepository.save(
                getLinkedMember(2L, PARTICIPANT, linkedWallet, member2));
        LinkedMember participant2 = linkedMemberRepository.save(
                getLinkedMember(3L, PARTICIPANT, linkedWallet, member3));
        SharedLinkCardCreateServiceRequest request = createSharedCards(LocalDate.now().plusDays(1),
                linkedWallet.getId().toString(),
                List.of(participant1.getId().toString(), participant2.getId().toString()));
        linkCardService.createShared(request, member1.getId());

        linkedMemberService.deleteLinkedMembers(linkedWallet.getId(),
                Set.of(participant1.getId(), participant2.getId()), member1.getId());

        assertAll(
                () -> assertThat(linkCardRepository.findByMemberIdAndWalletId(participant1.getId(),
                        linkedWallet.getId())).isEmpty(),
                () -> assertThat(linkCardRepository.findByMemberIdAndWalletId(participant2.getId(),
                        linkedWallet.getId())).isEmpty()
        );
    }

    private LinkedMember getLinkedMember(Long id, Role role, LinkedWallet linkedWallet, Member member) {
        return LinkedMember.builder()
                .id(id)
                .role(role)
                .linkedWallet(linkedWallet)
                .member(member)
                .build();
    }

    private LinkedWallet getLinkWallet(Long id, String name) {
        return LinkedWallet.builder()
                .id(id)
                .name(name)
                .build();
    }
}
