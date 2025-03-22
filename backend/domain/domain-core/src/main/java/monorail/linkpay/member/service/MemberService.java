package monorail.linkpay.member.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public void create(final Member member) {
        memberRepository.save(member);
    }
}
