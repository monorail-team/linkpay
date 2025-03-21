package monorail.linkpay.controller.request;

import jakarta.validation.constraints.Positive;

public record PointRequest(@Positive long amount) {
}
