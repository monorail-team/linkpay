package monorail.linkpay.controller.request;

import jakarta.validation.constraints.NotNull;

public record LinkedMemberCreateRequest(

        @NotNull(message = "참여자 아이디는 필수값입니다.")
        Long memberId
) {
}
