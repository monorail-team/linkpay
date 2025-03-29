package monorail.linkpay.wallet;

import static monorail.linkpay.exception.ExceptionCode.INVALID_REQUEST;
import static monorail.linkpay.wallet.domain.Role.CREATOR;
import static monorail.linkpay.wallet.domain.Role.PARTICIPANT;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import monorail.linkpay.common.IntegrationTest;
import monorail.linkpay.exception.LinkPayException;
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
