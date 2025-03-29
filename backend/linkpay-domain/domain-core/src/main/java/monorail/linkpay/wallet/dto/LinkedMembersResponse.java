package monorail.linkpay.wallet.dto;

import java.util.List;

public record LinkedMembersResponse(
        List<LinkedMemberResponse> linkedMembers,
        boolean hasNext
) {
}
