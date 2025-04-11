package monorail.linkpay.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.Instant;

@Builder
public record FcmRegisterRequest(

        @NotNull
        String token,
        @NotNull
        String deviceId
) {
}
