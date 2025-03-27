package monorail.linkpay.wallet.service;

import static monorail.linkpay.exception.ExceptionCode.INVALID_REQUEST;
import static monorail.linkpay.wallet.domain.Role.PARTICIPANT;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.member.service.MemberFetcher;
import monorail.linkpay.util.id.IdGenerator;
import monorail.linkpay.wallet.domain.LinkedMember;
import monorail.linkpay.wallet.domain.LinkedWallet;
import monorail.linkpay.wallet.repository.LinkedMemberRepository;
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
        validateCreatorPermission(linkedWalletId, memberId);
        Member member = memberFetcher.fetchById(participantId);
        LinkedWallet linkedWallet = linkedWalletFetcher.fetchById(linkedWalletId);
        LinkedMember linkedMember = LinkedMember.of(idGenerator.generate(), PARTICIPANT, member);

        linkedMemberRepository.save(LinkedMember.builder()
                .id(idGenerator.generate())
                .role(PARTICIPANT)
                .linkedWallet(linkedWallet)
                .member(member).build());

        linkedMemberRepository.save(linkedMember);
    }

    public void deleteLinkedMember(final Long linkedWalletId, final Set<Long> linkedMemberIds,
                                   final Long memberId, LocalDateTime now) {
        validateCreatorPermission(linkedWalletId, memberId);
        linkedWalletFetcher.checkExistsById(linkedWalletId);
        linkedMemberRepository.deleteByIds(linkedMemberIds, now);
    }

    private void validateCreatorPermission(final Long linkedWalletId, final Long memberId) {
        LinkedMember linkedMember = linkedMemberFetcher.fetchByLinkedWalletIdAndMemberId(linkedWalletId, memberId);
        if (!linkedMember.isCreator()) {
            throw new LinkPayException(INVALID_REQUEST, "지갑 생성자만 참여자를 관리할 수 있습니다.");
        }
    }
}
