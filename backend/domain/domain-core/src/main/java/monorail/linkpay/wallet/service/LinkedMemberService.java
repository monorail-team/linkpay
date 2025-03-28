package monorail.linkpay.wallet.service;

import static monorail.linkpay.exception.ExceptionCode.INVALID_REQUEST;
import static monorail.linkpay.wallet.domain.Role.PARTICIPANT;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.member.service.MemberFetcher;
import monorail.linkpay.util.id.IdGenerator;
import monorail.linkpay.wallet.domain.LinkedMember;
import monorail.linkpay.wallet.domain.LinkedWallet;
import monorail.linkpay.wallet.dto.LinkedMemberResponse;
import monorail.linkpay.wallet.dto.LinkedMembersResponse;
import monorail.linkpay.wallet.repository.LinkedMemberRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LinkedMemberService {

    private final LinkedMemberRepository linkedMemberRepository;
    private final LinkedMemberFetcher linkedMemberFetcher;
    private final LinkedWalletFetcher linkedWalletFetcher;
    private final MemberFetcher memberFetcher;
    private final IdGenerator idGenerator;

    public LinkedMembersResponse getLinkedMembers(final Long linkedWalletId, final Long lastId, final int size) {
        Slice<LinkedMember> linkedMembers = linkedMemberRepository.findAllByLinkedWalletId(
                linkedWalletId, lastId, PageRequest.of(0, size));
        return new LinkedMembersResponse(linkedMembers.stream()
                .map(LinkedMemberResponse::from)
                .toList(),
                linkedMembers.hasNext());
    }

    @Transactional
    public void createLinkedMember(final Long linkedWalletId, final Long memberId, final Long participantId) {
        validateCreatorPermission(linkedWalletId, memberId);
        Member member = memberFetcher.fetchById(participantId);
        LinkedWallet linkedWallet = linkedWalletFetcher.fetchById(linkedWalletId);

        linkedMemberRepository.save(LinkedMember.builder()
                .id(idGenerator.generate())
                .role(PARTICIPANT)
                .linkedWallet(linkedWallet)
                .member(member).build());
    }

    @Transactional
    public void deleteLinkedMember(final Long linkedWalletId, final Set<Long> linkedMemberIds, final Long memberId) {
        validateCreatorPermission(linkedWalletId, memberId);
        linkedWalletFetcher.checkExistsById(linkedWalletId);
        linkedMemberRepository.deleteByIds(linkedMemberIds);
    }

    private void validateCreatorPermission(final Long linkedWalletId, final Long memberId) {
        LinkedMember linkedMember = linkedMemberFetcher.fetchByLinkedWalletIdAndMemberId(linkedWalletId, memberId);
        if (!linkedMember.isCreator()) {
            throw new LinkPayException(INVALID_REQUEST, "지갑 생성자만 참여자를 관리할 수 있습니다.");
        }
    }
}
