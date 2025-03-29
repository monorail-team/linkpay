package monorail.linkpay.wallet.dto;

import monorail.linkpay.wallet.domain.LinkedMember;

public record LinkedMemberResponse(
        String linkedMemberId,
        String name,
        String email
) {
    public static LinkedMemberResponse from(final LinkedMember linkedMember) {
        return new LinkedMemberResponse(
                linkedMember.getId().toString(),
                linkedMember.getMember().getUsername(),
                linkedMember.getMember().getEmail()
        );
    }
}
