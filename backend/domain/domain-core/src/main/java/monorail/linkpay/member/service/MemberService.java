package monorail.linkpay.member.service;

import monorail.linkpay.member.domain.Member;
import monorail.linkpay.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public void create(Member member) {
        memberRepository.save(member);
    }
    public List<Member> getMembers() {
        return memberRepository.findAll();
    }
}
