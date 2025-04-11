package monorail.linkpay.member.dto;

import java.util.List;

public record EmailsResponse(
        List<String> emails
) {
}
