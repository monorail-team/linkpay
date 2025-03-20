package monorail.linkpay.controller.request;

import jakarta.validation.constraints.Positive;

public record ChargeRequest(@Positive long amount) {
}
