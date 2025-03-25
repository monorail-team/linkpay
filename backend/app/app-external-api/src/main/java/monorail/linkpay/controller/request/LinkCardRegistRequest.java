package monorail.linkpay.controller.request;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record LinkCardRegistRequest(

        @NotEmpty(message = "하나 이상의 카드를 선택해주세요.")
        List<Long> linkCardIds
) {
}
