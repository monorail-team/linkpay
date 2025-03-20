package monorail.linkpay.controller.request;

import jakarta.validation.constraints.Positive;

public record DeductRequest(@Positive long amount) {
}
