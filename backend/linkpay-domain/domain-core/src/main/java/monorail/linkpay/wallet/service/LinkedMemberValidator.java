package monorail.linkpay.wallet.service;

import static monorail.linkpay.exception.ExceptionCode.NOT_FOUND_RESOURCE;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.wallet.repository.LinkedMemberRepository;

@SupportLayer
@RequiredArgsConstructor
public class LinkedMemberValidator {
    private final LinkedMemberRepository linkedMemberRepository;

    public void validateIsLinkedMember(Long linkedWalletId, long memberId) {
        if (!linkedMemberRepository.existsByLinkedWalletIdAndMemberId(linkedWalletId, memberId)) {
            throw new LinkPayException(NOT_FOUND_RESOURCE, "요청한 아이디에 해당하는 링크 멤버가 존재하지 않습니다.");
        }
    }

    public void validateIsLinkedMember(Long linkedWalletId, long memberId, LinkPayException e) {
        if (!linkedMemberRepository.existsByLinkedWalletIdAndMemberId(linkedWalletId, memberId)) {
            throw e;
        }
    }
}
