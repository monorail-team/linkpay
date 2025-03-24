package monorail.linkpay.linkedwallet.service;

import static monorail.linkpay.linkedwallet.domain.Role.CREATOR;
import static monorail.linkpay.linkedwallet.domain.Role.PARTICIPANT;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.linkedwallet.domain.LinkedMember;
import monorail.linkpay.linkedwallet.domain.LinkedWallet;
import monorail.linkpay.linkedwallet.repository.LinkedWalletRepository;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.member.repository.MemberRepository;
import monorail.linkpay.member.service.MemberFetcher;
import monorail.linkpay.util.id.IdGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LinkedWalletService {

    private final LinkedWalletRepository linkedWalletRepository;
    private final MemberRepository memberRepository;
    private final MemberFetcher memberFetcher;
    private final IdGenerator idGenerator;

    public Long createLinkedWallet(final long memberId, final String walletName,
                                   final Set<Long> memberIds) {
        Member member = memberFetcher.fetchById(memberId);
        LinkedWallet linkedWallet = LinkedWallet.builder()
                .id(idGenerator.generate())
                .name(walletName)
                .build();

        linkedWallet.registerLinkedMember(LinkedMember.of(member, idGenerator.generate(), CREATOR));
        memberRepository.findMembersByIdIn(memberIds).forEach(tuple ->
                linkedWallet.registerLinkedMember(LinkedMember.of(tuple, idGenerator.generate(), PARTICIPANT)));

        linkedWalletRepository.save(linkedWallet);
        return linkedWallet.getId();
    }
}
