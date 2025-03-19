package monorail.linkpay.member.service;

import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.exception.ExceptionCode;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@SupportLayer
@RequiredArgsConstructor
public class MemberFetcher {

    private final MemberRepository memberRepository;

    public Member fetchBy(final String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(() -> new LinkPayException(ExceptionCode.NOT_FOUND_RESOURCE, "Member not found"));
    }

    public Member fetchById(final Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new LinkPayException(ExceptionCode.NOT_FOUND_RESOURCE, "Member not found"));
    }
}
