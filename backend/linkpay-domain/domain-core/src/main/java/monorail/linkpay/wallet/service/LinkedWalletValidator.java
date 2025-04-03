package monorail.linkpay.wallet.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.linkcard.service.LinkCardFetcher;
import monorail.linkpay.wallet.domain.LinkedMember;

import java.util.List;
import java.util.Set;

import static monorail.linkpay.exception.ExceptionCode.INVALID_REQUEST;

@SupportLayer
@RequiredArgsConstructor
public class LinkedWalletValidator {

    private final LinkedMemberValidator linkedMemberValidator;
    private final LinkedMemberFetcher linkedMemberFetcher;
    private final LinkCardFetcher linkCardFetcher;

    public void validateCreate(final Long linkedWalletId, final Set<Long> memberIds) {
        checkCreatorNotInParticipants(linkedWalletId, memberIds);
    }

    public void validateRead(final Long linkedWalletId, long memberId) {
        linkedMemberValidator.validateIsLinkedMember(linkedWalletId, memberId);
    }

    public void validateCharge(final Long linkedWalletId,final Long memberId) {
        linkedMemberValidator.validateIsLinkedMember(linkedWalletId, memberId);
    }

    public void validateDelete(final Long linkedWalletId, final Long memberId) {
        LinkedMember linkedMember = linkedMemberFetcher.fetchByLinkedWalletIdAndMemberId(linkedWalletId, memberId);
        checkCreator(linkedMember);
        checkNotExistsByWalletId(linkedWalletId);
    }

    private void checkNotExistsByWalletId(final Long walletId) {
        List<LinkCard> linkCards = linkCardFetcher.fetchAllByWalletId(walletId);
        if (!linkCards.isEmpty()) {
            throw new LinkPayException(INVALID_REQUEST, "해당 지갑에 링크카드가 존재합니다.");
        }
    }

    private void checkCreatorNotInParticipants(final Long creatorId, final Set<Long> memberIds) {
        if (memberIds.contains(creatorId)) {
            throw new LinkPayException(INVALID_REQUEST, "본인을 참여자 목록에 포함할 수 없습니다.");
        }
    }

    private void checkCreator(final LinkedMember linkedMember) {
        if (!linkedMember.isCreator()) {
            throw new LinkPayException(INVALID_REQUEST, "링크 지갑은 생성자만 삭제할 수 있습니다.");
        }
    }
}
