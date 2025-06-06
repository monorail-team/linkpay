package monorail.linkpay.member.service;

import static monorail.linkpay.exception.ExceptionCode.NOT_FOUND_RESOURCE;

import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.member.repository.MemberRepository;

@SupportLayer
@RequiredArgsConstructor
public class MemberFetcher {

    private final MemberRepository memberRepository;

    public Member fetchByEmail(final String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new LinkPayException(NOT_FOUND_RESOURCE, "요청한 이메일에 해당하는 회원이 존재하지 않습니다."));
    }

    public Member fetchById(final Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new LinkPayException(NOT_FOUND_RESOURCE, "요청한 아이디에 해당하는 회원이 존재하지 않습니다."));
    }

    public List<Member> fetchByIdIn(final Set<Long> memberIds) {
        return memberRepository.findMembersByIdIn(memberIds);
    }

    public List<Member> fetchAllByEmailLike(String keyword) {
        return memberRepository.findByEmailStartsWith(keyword);
    }
}
