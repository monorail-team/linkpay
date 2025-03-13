package backend.a105.member.service;

import backend.a105.member.domain.Member;
import backend.a105.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public List<Member> getMembers() {
        return memberRepository.findAll();
    }
}
