package monorail.linkpay.linkedwallet.service;

import static monorail.linkpay.exception.ExceptionCode.NOT_FOUND_RESOURCE;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.linkedwallet.domain.LinkedMember;
import monorail.linkpay.linkedwallet.repository.LinkedMemberRepository;

@SupportLayer
@RequiredArgsConstructor
public class LinkedMemberFetcher {

    private final LinkedMemberRepository linkedMemberRepository;

    public LinkedMember fetchById(final Long id) {
        return linkedMemberRepository.findById(id)
                .orElseThrow(() ->
                        new LinkPayException(NOT_FOUND_RESOURCE, "요청한 아이디에 해당하는 링크 멤버가 존재하지 않습니다."));
    }

    public LinkedMember fetchByMemberId(final Long memberId) {
        return linkedMemberRepository.findByMemberId(memberId)
                .orElseThrow(() ->
                        new LinkPayException(NOT_FOUND_RESOURCE, "요청한 멤버 아이디에 해당하는 링크 멤버가 존재하지 않습니다."));
    }
}
