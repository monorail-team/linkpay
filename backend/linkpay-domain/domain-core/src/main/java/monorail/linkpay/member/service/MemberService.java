package monorail.linkpay.member.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.member.dto.MemberResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberFetcher memberFetcher;

    public MemberResponse getMember(final String email) {
        Member member = memberFetcher.fetchByEmail(email);
        return MemberResponse.from(member);
    }
}
