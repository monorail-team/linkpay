package monorail.linkpay.wallet.service;

import static monorail.linkpay.exception.ExceptionCode.DUPLICATED_RESOURCE;
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
    private final LinkedMemberValidator linkedMemberValidator;

    public LinkedMembersResponse getLinkedMembers(final Long linkedWalletId, final long memberId,
                                                  final Long lastId, final int size) {
        linkedMemberValidator.validateIsLinkedMember(linkedWalletId, memberId);
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
        validateDuplicatedLinkedMember(linkedWalletId, participantId);
        if (linkedMemberRepository.restoreDeletedLinkedMember(participantId, linkedWalletId) > 0) {
            return;
        }
        Member member = memberFetcher.fetchById(participantId);
        LinkedWallet linkedWallet = linkedWalletFetcher.fetchById(linkedWalletId);

        linkedMemberRepository.save(LinkedMember.builder()
                .id(idGenerator.generate())
                .role(PARTICIPANT)
                .linkedWallet(linkedWallet)
                .member(member)
                .build());
    }

    @Transactional
    public void deleteLinkedMember(final Long linkedWalletId, final Long linkedMemberId, final long memberId) {
        linkedWalletFetcher.checkExistsById(linkedWalletId);
        LinkedMember linkedMember = linkedMemberFetcher.fetchByLinkedWalletIdAndMemberId(linkedWalletId, memberId);
        validateCreatorOrSelf(memberId, linkedMember);

        LinkedMember target = linkedMemberFetcher.fetchById(linkedMemberId);
        if (target.isCreator()) {
            throw new LinkPayException(INVALID_REQUEST, "링크지갑 생성자를 삭제할 수 없습니다.");
        }
        linkedMemberRepository.delete(target);
    }

    @Transactional
    public void deleteLinkedMembers(final Long linkedWalletId, final Set<Long> linkedMemberIds, final Long memberId) {
        linkedWalletFetcher.checkExistsById(linkedWalletId);
        LinkedMember linkedMember = linkedMemberFetcher.fetchByLinkedWalletIdAndMemberId(linkedWalletId, memberId);

        if (!linkedMember.isCreator()) {
            throw new LinkPayException(INVALID_REQUEST, "링크지갑 생성자만 참여자를 관리할 수 있습니다.");
        }
        validateNotDeletingCreator(linkedMemberIds, linkedMember.getId());
        linkedMemberRepository.deleteByIds(linkedMemberIds);
    }

    private void validateCreatorPermission(final Long linkedWalletId, final Long memberId) {
        LinkedMember linkedMember = linkedMemberFetcher.fetchByLinkedWalletIdAndMemberId(linkedWalletId, memberId);
        if (!linkedMember.isCreator()) {
            throw new LinkPayException(INVALID_REQUEST, "링크지갑 생성자만 참여자를 관리할 수 있습니다.");
        }
    }

    private void validateCreatorOrSelf(final long memberId, final LinkedMember linkedMember) {
        if (!linkedMember.isCreator() && !linkedMember.getMember().getId().equals(memberId)) {
            throw new LinkPayException(INVALID_REQUEST, "링크지갑 생성자 또는 본인만이 삭제할 수 있습니다.");
        }
    }

    private void validateNotDeletingCreator(final Set<Long> linkedMemberIds, final Long linkedCreatorId) {
        if (linkedMemberIds.contains(linkedCreatorId)) {
            throw new LinkPayException(INVALID_REQUEST, "링크지갑 생성자는 자신을 삭제할 수 없습니다.");
        }
    }

    private void validateDuplicatedLinkedMember(final Long linkedWalletId, final Long participantId) {
        linkedMemberRepository.findByLinkedWalletIdAndMemberId(linkedWalletId, participantId)
                .ifPresent(linkedMember -> {
                    throw new LinkPayException(DUPLICATED_RESOURCE, "이미 링크 지갑에 가입된 회원입니다.");
                });
    }
}
