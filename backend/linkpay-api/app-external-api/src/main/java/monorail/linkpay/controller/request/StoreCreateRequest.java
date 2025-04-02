package monorail.linkpay.controller.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record StoreCreateRequest(

        @NotEmpty(message = "가게 이름을 입력해주세요.")
        @Size(max = 50, message = "가게 이름은 50자를 넘을 수 없습니다.")
        String storeName
) {
}
