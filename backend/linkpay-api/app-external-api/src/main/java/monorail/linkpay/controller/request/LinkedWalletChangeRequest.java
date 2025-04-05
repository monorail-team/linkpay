package monorail.linkpay.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LinkedWalletChangeRequest(
        @NotBlank(message = "링크 지갑 이름을 입력해주세요.")
        @Size(max = 50, message = "지갑 이름은 50자를 넘을 수 없습니다.")
        String linkedWalletName
) {
}
