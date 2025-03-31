package monorail.linkpay.wallet.service;

import static monorail.linkpay.exception.ExceptionCode.NOT_FOUND_RESOURCE;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.wallet.domain.LinkedMember;
import monorail.linkpay.wallet.repository.LinkedMemberRepository;

@SupportLayer
@RequiredArgsConstructor
public class LinkedMemberFetcher {

    private final LinkedMemberRepository linkedMemberRepository;

    public void checkExistsByLinkedWalletIdAndMemberId(final Long linkedWalletId, final Long memberId) {
        if (!linkedMemberRepository.existsByLinkedWalletIdAndMemberId(linkedWalletId, memberId)) {
            throw new LinkPayException(NOT_FOUND_RESOURCE, "요청한 아이디에 해당하는 링크 멤버가 존재하지 않습니다.");
        }
    }

    public LinkedMember fetchById(final Long id) {
        return linkedMemberRepository.findById(id)
                .orElseThrow(() -> new LinkPayException(NOT_FOUND_RESOURCE, "요청한 아이디에 해당하는 링크 멤버가 존재하지 않습니다."));
    }

    public LinkedMember fetchByLinkedWalletIdAndMemberId(final Long linkedWalletId, final Long memberId) {
        return linkedMemberRepository.findByLinkedWalletIdAndMemberId(linkedWalletId, memberId)
                .orElseThrow(() ->
                        new LinkPayException(NOT_FOUND_RESOURCE, "요청한 멤버 아이디에 해당하는 링크 멤버가 존재하지 않습니다."));
    }

    public LinkedMember fetchByMemberId(final Long memberId) {
        return linkedMemberRepository.findByMemberId(memberId)
                .orElseThrow(() ->
                        new LinkPayException(NOT_FOUND_RESOURCE, "요청한 멤버 아이디에 해당하는 링크 멤버가 존재하지 않습니다."));
    }
}
