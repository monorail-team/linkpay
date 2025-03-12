package backend.a105.member.service;

import backend.a105.exception.AppException;
import backend.a105.exception.ExceptionCode;
import backend.a105.annotation.SupportLayer;
import backend.a105.member.domain.Member;
import backend.a105.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@SupportLayer
@RequiredArgsConstructor
public class MemberFetcher {
    private final MemberRepository memberRepository;

    public Member fetchBy(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ExceptionCode.NOT_FOUND_RESOURCE, "Member not found"));
    }
}
