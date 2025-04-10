package monorail.linkpay.member.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.algo.trie.PrefixTrie;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.member.dto.EmailsResponse;
import monorail.linkpay.member.dto.MemberResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberFetcher memberFetcher;
    private final PrefixTrie cachedEmails = new PrefixTrie();

    // todo: 본인 검증
    public MemberResponse getMember(final String email) {
        Member member = memberFetcher.fetchByEmail(email);
        return MemberResponse.from(member);
    }

    public MemberResponse getMember(final Long memberId) {
        Member member = memberFetcher.fetchById(memberId);
        return MemberResponse.from(member);
    }

    public EmailsResponse searchEmail(String keyword, int size) {
        List<String> emails = cachedEmails.searchAll(keyword);

        if (emails.isEmpty()) {
            emails = memberFetcher.fetchAllByEmailLike(keyword).stream().map(Member::getEmail).toList();
            emails.forEach(cachedEmails::insert);
        }

        return new EmailsResponse(emails.subList(0, Math.min(emails.size(), size)));
    }
}
