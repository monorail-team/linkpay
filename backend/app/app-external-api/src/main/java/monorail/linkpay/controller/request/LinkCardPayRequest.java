package monorail.linkpay.controller.request;

import jakarta.validation.constraints.Positive;

public record LinkCardPayRequest(

        @Positive(message = "금액은 양수여야 합니다.")
        long amount,
        Long linkCardId,
        String merchantName
) {
}
