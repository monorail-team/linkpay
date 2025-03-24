package monorail.linkpay.linkedwallet.service;

import static java.time.LocalDateTime.now;
import static monorail.linkpay.exception.ExceptionCode.INVALID_REQUEST;
import static monorail.linkpay.linkedwallet.domain.Role.PARTICIPANT;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.linkedwallet.domain.LinkedMember;
import monorail.linkpay.linkedwallet.domain.LinkedWallet;
import monorail.linkpay.linkedwallet.repository.LinkedMemberRepository;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.member.service.MemberFetcher;
import monorail.linkpay.util.id.IdGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LinkedMemberService {

    private final LinkedMemberRepository linkedMemberRepository;
    private final LinkedMemberFetcher linkedMemberFetcher;
    private final LinkedWalletFetcher linkedWalletFetcher;
    private final MemberFetcher memberFetcher;
    private final IdGenerator idGenerator;

    public void createLinkedMember(final Long linkedWalletId, final Long memberId, final Long participantId) {
        validateCreatorPermission(memberId);

        LinkedWallet linkedWallet = linkedWalletFetcher.fetchById(linkedWalletId);
        Member member = memberFetcher.fetchById(participantId);
        LinkedMember linkedMember = LinkedMember.of(member, idGenerator.generate(), PARTICIPANT);

        linkedWallet.registerLinkedMember(linkedMember);
        linkedMemberRepository.save(linkedMember);
    }

    public void deleteLinkedMember(final Long linkedWalletId, final Set<Long> linkedMemberIds, final Long memberId) {
        validateCreatorPermission(memberId);
        linkedWalletFetcher.checkExistsById(linkedWalletId);
        linkedMemberRepository.deleteByIds(linkedMemberIds, now());
    }

    private void validateCreatorPermission(Long memberId) {
        LinkedMember linkedMember = linkedMemberFetcher.fetchByMemberId(memberId);
        if (!linkedMember.isCreator()) {
            throw new LinkPayException(INVALID_REQUEST, "지갑 생성자만 참여자를 관리할 수 있습니다.");
        }
    }
}
