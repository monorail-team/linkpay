package monorail.linkpay.member;

import monorail.linkpay.member.domain.Member;

import java.util.concurrent.atomic.AtomicLong;

public class MemberFixtures {
    private static final AtomicLong idGenerator = new AtomicLong();

    public static Member member() {
        return Member.builder()
                .id(idGenerator.getAndIncrement())
                .username("link")
                .email("email@kakao.com").build();
    }
}
