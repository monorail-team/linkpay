package monorail.linkpay.controller.request;

import jakarta.validation.constraints.Positive;

public record StoreTransactionRequest(

        @Positive(message = "금액은 양수여야 합니다.")
        long amount
) {
}
