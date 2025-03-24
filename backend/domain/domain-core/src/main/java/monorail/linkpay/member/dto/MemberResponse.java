package monorail.linkpay.member.dto;

import monorail.linkpay.member.domain.Member;

public record MemberResponse(
        Long memberId,
        String username,
        String email
) {
    public static MemberResponse from(final Member member) {
        return new MemberResponse(member.getId(), member.getUsername(), member.getEmail());
    }
}
